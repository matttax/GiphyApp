package com.matttax.giphyvk

data class SearchConfig(
    var offset: Int,
    var searchType: SearchType,
    var searchText: String?
) {
    fun toDefault() {
        offset = 0
        searchText = null
        searchType = SearchType.TRENDING
    }
}

enum class SearchType {
    TRENDING,
    BY_TEXT
}