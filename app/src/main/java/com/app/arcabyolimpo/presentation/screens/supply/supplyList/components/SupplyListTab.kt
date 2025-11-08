package com.app.arcabyolimpo.presentation.screens.supply.supplyList.components

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
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = isLoading,
            onRefresh = onRetry,
        )
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
    ) {
        when {
            isLoading && suppliesList.isEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(10) {
                        LoadingShimmer(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                        )
                    }
                }
            }
            error != null && suppliesList.isEmpty() -> {
                ErrorView(
                    message = error,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(16.dp),
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
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            scale = true,
        )
    }
}
