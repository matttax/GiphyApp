package com.matttax.giphyapp

data class SearchConfig(
    val offset: Int,
    val searchType: SearchType,
    val searchText: String?
) {
    companion object {
        fun getDefault() = SearchConfig(0, SearchType.TRENDING, null)
    }
}

enum class SearchType {
    TRENDING,
    BY_TEXT
}
