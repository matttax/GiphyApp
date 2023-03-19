package com.matttax.giphyvk.retrofit

import com.google.gson.annotations.SerializedName

data class GifListData(@SerializedName("data") val images: List<GeneralData>)

data class SingleGifData(@SerializedName("data") val image: GeneralData)

data class GeneralData(@SerializedName("images") val imageData: ImageData, val title: String)

data class ImageData(@SerializedName("original") val gifImage: GifImage)

data class GifImage(val url: String)