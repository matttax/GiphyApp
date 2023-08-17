package com.matttax.giphyapp.network

import com.google.gson.annotations.SerializedName

data class GifListData(
    @SerializedName("data")
    val images: List<GeneralGifData>
)

data class SingleGifData(
    @SerializedName("data")
    val image: GeneralGifData
)

data class GeneralGifData(
    @SerializedName("images")
    val imageData: ImageData,

    @SerializedName("title")
    val title: String,
)

data class ImageData(
    @SerializedName("original")
    val gifImage: GifImage
)

data class GifImage(
    val url: String
)
