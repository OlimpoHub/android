package com.app.arcabyolimpo.domain.model.filter

/**
 * Represents the set of available filter options for supplies.
 *
 * This class groups all possible filter sections (e.g., "Categorías", "Medidas", "Talleres")
 * into a map, where each key corresponds to a section title and each value is the list
 * of selectable options under that category.
 *
 * @property sections A map where the key is the section title and the value is a list of option strings.
 */
data class FilterData(
    val sections: Map<String, List<String>>,
) {
    /**
     * Converts the [sections] map into a list of [Section] objects.
     *
     * This function is useful for rendering filter sections in the UI,
     * where each section (title + list of options) is displayed as a group.
     *
     * @return A list of [Section] instances derived from the [sections] map.
     */
    fun asSections(): List<Section> =
        sections.map { (title, options) ->
            Section(title, options)
        }
}

/**
 * Represents a single section of filter options.
 *
 * Each [Section] contains a descriptive [title] and a list of [items]
 * that can be displayed or selected by the user in the UI.
 *
 * @property title The title or label of the section (e.g., "Categorías").
 * @property items The list of selectable options within this section.
 */
data class Section(
    val title: String,
    val items: List<String>,
)
