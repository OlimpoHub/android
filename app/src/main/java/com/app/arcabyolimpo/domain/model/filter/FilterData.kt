package com.app.arcabyolimpo.domain.model.filter

data class FilterData(
    val sections: Map<String, List<String>>,
) {
    fun asSections(): List<Section> =
        sections.map { (title, options) ->
            Section(title, options)
        }
}

data class Section(
    val title: String,
    val items: List<String>,
)
