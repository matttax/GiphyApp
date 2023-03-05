package com.matttax.giphyvk

import com.google.gson.annotations.SerializedName;
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class DataResult(@SerializedName("data") val res: List<DataObject>)

data class DataObject(@SerializedName("images") val images: DataImage)

data class DataImage(@SerializedName("original") val ogImage: OgImage)

data class OgImage(val url: String)

interface DataService {
    @GET("gifs/trending?api_key=tFK8ltPBTPVkWnBQC5AGspOKESkOehq9&limit=25")
    fun getTrendingGifs(@Query("offset") offset: Int): retrofit2.Call<DataResult>

    @GET("gifs/search?api_key=tFK8ltPBTPVkWnBQC5AGspOKESkOehq9&limit=25&rating=g&lang=en")
    fun searchGifs(@Query("q") search: String, @Query("offset") offset: Int): retrofit2.Call<DataResult>
}