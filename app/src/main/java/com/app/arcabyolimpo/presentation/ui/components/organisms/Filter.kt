
@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.data.remote.dto.filter.createFilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ApplyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteAllButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ExitIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.MinusIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.PlusIcon
import com.app.arcabyolimpo.ui.theme.HeaderBackground
import com.app.arcabyolimpo.ui.theme.PlaceholderGray
import com.app.arcabyolimpo.ui.theme.White
import kotlin.collections.forEach

/**
 * Displays a modal bottom sheet that allows users to filter and order supplies.
 *
 * This composable shows a customizable filter interface where users can:
 * - Select multiple filter options (e.g., categories, measures, workshops)
 * - Choose an order for the results (ascending or descending)
 * - Apply, clear, or dismiss the filter sheet
 *
 * The component uses `FilterData` to dynamically build filter sections
 * and keeps track of selected values using Compose state holders.
 *
 * @param data The available filter data grouped by section (e.g., categories, measures, workshops).
 * @param initialSelected The initial filter and order state applied when the bottom sheet opens.
 * @param onApply Callback triggered when the user applies filters. Receives a [FilterDto] with selected filters and order.
 * @param onDismiss Callback triggered when the bottom sheet is dismissed without applying filters.
 * @param onClearFilters Callback triggered when the user clears all selected filters.
 *
 * Example usage:
 * ```
 * Filter(
 *             data = uiState.filterData!!,
 *             initialSelected = uiState.selectedFilters,
 *             onApply = { dto ->
 *                 viewModel.filterSupplies(dto)
 *                 showFilter = false
 *             },
 *             onDismiss = { showFilter = false },
 *             onClearFilters = {
 *                 viewModel.clearFilters()
 *                 viewModel.loadSuppliesList()
 *                 showFilter = false
 *             },
 *         )
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter(
    data: FilterData,
    initialSelected: FilterDto,
    onApply: (FilterDto) -> Unit,
    onDismiss: () -> Unit,
    onClearFilters: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val selectedOrder =
        remember(initialSelected.order) {
            mutableStateOf(initialSelected.order ?: "")
        }

    val sections = data.asSections()
    println(data)

    val selectedMap =
        remember {
            mutableStateMapOf<String, SnapshotStateList<String>>().apply {
                sections.forEach { section ->
                    val initial = initialSelected.filters[section.title].orEmpty()
                    this[section.title] = initial.toMutableStateList()
                }
            }
        }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = HeaderBackground,
        contentColor = White,
        scrimColor = HeaderBackground,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 750.dp), // ajústalo al tamaño que quieras
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    FilterIcon(tint = White, size = 35.dp)

                    Spacer(
                        modifier =
                            Modifier
                                .width(8.dp)
                                .padding(top = 50.dp),
                    )

                    Text(
                        "Filtrar & Ordenar",
                        style = Typography.headlineMedium,
                        modifier = Modifier.padding(top = 10.dp),
                    )

                    Spacer(
                        modifier =
                            Modifier
                                .padding(end = 115.dp),
                    )

                    ExitIcon(
                        modifier =
                            Modifier
                                .padding(top = 5.dp)
                                .clickable {
                                    onDismiss()
                                },
                        tint = White,
                        size = 25.dp,
                    )
                }

                Text(
                    "Filtrar",
                    style = Typography.headlineMedium,
                    modifier = Modifier.padding(top = 10.dp),
                )

                HorizontalDivider(
                    modifier =
                        Modifier
                            .padding(end = 40.dp),
                    thickness = 1.dp,
                    color = White,
                )

                sections.forEach { (title, items) ->
                    FilterExpandableSection(
                        title = title,
                        items = items,
                        // Passes the list of selected items for this section from the shared state map.
                        // The '!!' ensures the key exists, since all sections were initialized in 'selectedMap'.
                        selectedItems = selectedMap[title]!!,
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp),
                )

                Text(
                    "Ordenar",
                    style = Typography.headlineMedium,
                )

                HorizontalDivider(
                    modifier =
                        Modifier
                            .padding(end = 40.dp),
                    thickness = 1.dp,
                    color = White,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 30.dp),
                ) {
                    RadioButton(
                        selected = selectedOrder.value == "ASC",
                        onClick = { selectedOrder.value = "ASC" },
                        colors =
                            RadioButtonDefaults.colors(
                                selectedColor = White,
                                unselectedColor = PlaceholderGray,
                            ),
                    )
                    Text("Ascendente")

                    RadioButton(
                        selected = selectedOrder.value == "DESC",
                        onClick = { selectedOrder.value = "DESC" },
                        colors =
                            RadioButtonDefaults.colors(
                                selectedColor = White,
                                unselectedColor = PlaceholderGray,
                            ),
                        modifier = Modifier.padding(start = 20.dp),
                    )
                    Text("Descendente")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(
                        modifier =
                            Modifier
                                .padding(end = 30.dp),
                    )

                    DeleteAllButton(
                        onClick = {
                            selectedMap.forEach { (_, list) -> list.clear() }
                            onClearFilters()
                        },
                    )

                    Spacer(
                        modifier =
                            Modifier
                                .padding(end = 15.dp),
                    )

                    ApplyButton(
                        onClick = {
                            println("SELECTED MAP : $selectedMap")

                            // Create a FilterDto object from the user's selected filters and order,
                            // then pass it to the onApply callback to trigger filtering logic in the ViewModel.
                            val dto = createFilterDto(selectedMap, selectedOrder.value)
                            onApply(dto)
                            onDismiss()
                        },
                    )

                    Spacer(
                        modifier =
                            Modifier
                                .height(20.dp),
                    )
                }
            }
        }
    }
}

/**
 * Displays an expandable section used for filtering options within the filter bottom sheet.
 *
 * Each section represents a filter category (e.g., "Categorías", "Medidas", "Talleres")
 * that can be expanded or collapsed. When expanded, it shows a list of selectable
 * items with checkboxes, allowing multiple selections.
 *
 * The state of selected items is managed externally through the `selectedItems` list,
 * which is updated whenever the user checks or unchecks an option.
 *
 * @param title The title of the filter section (e.g., "Categorías").
 * @param items The list of available filter options displayed in this section.
 * @param selectedItems A mutable list that holds the items currently selected by the user.
 *
 * Example usage:
 * ```
 * FilterExpandableSection(
 *     title = "Categorías",
 *     items = listOf("Herramientas", "Materiales", "Seguridad"),
 *     selectedItems = selectedCategories
 * )
 * ```
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun FilterExpandableSection(
    title: String,
    items: List<String>,
    selectedItems: MutableList<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(end = 40.dp)
                    .clickable { expanded = !expanded },
        ) {
            Text(
                text = title,
                style = Typography.headlineSmall,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (expanded) {
                MinusIcon(
                    modifier =
                        Modifier
                            .padding(end = 10.dp),
                    tint = White,
                    size = 12.dp,
                )
            } else {
                PlusIcon(
                    modifier =
                        Modifier
                            .padding(end = 10.dp),
                    tint = White,
                    size = 12.dp,
                )
            }
        }

        if (expanded) {
            Column(
                modifier = Modifier.padding(top = 4.dp),
            ) {
                items.forEach { item ->
                    val checked = item in selectedItems

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (checked) {
                                        selectedItems.remove(item)
                                    } else {
                                        selectedItems.add(item)
                                    }
                                }.padding(vertical = 4.dp),
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedItems.add(item)
                                } else {
                                    selectedItems.remove(item)
                                }
                            },
                            colors =
                                CheckboxDefaults.colors(
                                    checkedColor = White,
                                    uncheckedColor = PlaceholderGray,
                                    checkmarkColor = White,
                                ),
                        )

                        Text(
                            text = item,
                            style = Typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}
