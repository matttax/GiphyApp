package com.matttax.giphyapp.model

import com.matttax.giphyapp.network.GeneralGifData

data class GifModel(
    val title: String,
    val url: String,
    val isInFavourites: Boolean
) {
    companion object {
        fun GeneralGifData.toGifModel(): GifModel {
            return GifModel(
                title = this.title,
                url = this.imageData.gifImage.url,
                isInFavourites = false
            )
        }
    }
}
