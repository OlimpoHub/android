package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.common.components.ErrorView
import com.app.arcabyolimpo.presentation.common.components.LoadingShimmer
import com.app.arcabyolimpo.presentation.ui.components.molecules.SupplyCard

/**
 * Displays the main content of the Supplies List screen.
 *
 * This composable handles different UI states (loading, error, or success)
 * and shows the appropriate view for each. It also supports a pull-to-refresh
 * interaction to reload the data.
 *
 * ### UI States
 * - **Loading:** Displays animated shimmer placeholders.
 * - **Error:** Shows an error message and a retry button.
 * - **Success:** Displays the list of supplies in a scrollable grid.
 *
 * @param suppliesList The list of [Supply] items to display.
 * @param isLoading Indicates whether the data is currently loading.
 * @param error A nullable string describing an error message (if any).
 * @param onSupplyClick Callback invoked when a specific [Supply] card is clicked.
 * @param onRetry Callback triggered when the user requests a retry (e.g., pull-to-refresh or error view button).
 * @param modifier Optional [Modifier] to customize the layout appearance of the parent container.
 *
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SupplyListContent(
    suppliesList: List<Supply>,
    isLoading: Boolean,
    error: String?,
    onSupplyClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Remember the state for the pull-to-refresh component.
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = isLoading,
            onRefresh = onRetry,
        )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
    ) {
        when {
            // Show shimmer placeholders while loading and no data is yet available.
            isLoading && suppliesList.isEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(10) {
                        LoadingShimmer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                        )
                    }
                }
            }

            // Show an error message if loading fails and no data is available.
            error != null && suppliesList.isEmpty() -> {
                ErrorView(
                    message = error,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            // Show the list of supplies once successfully loaded.
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(
                        items = suppliesList,
                        key = { it.id },
                    ) { supply ->
                        SupplyCard(
                            supply = supply,
                            onClick = { onSupplyClick(supply.id) },
                        )
                    }
                }
            }
        }

        // Displays the pull-to-refresh indicator at the top center of the screen.
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            scale = true,
        )
    }
}
