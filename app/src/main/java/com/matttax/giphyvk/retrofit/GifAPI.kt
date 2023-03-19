package com.matttax.giphyvk.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GifAPI {
    @GET("gifs/random?api_key=$apiKey")
    fun getRandomGif(): Call<SingleGifData>

    @GET("gifs/trending?api_key=$apiKey&limit=24")
    fun getTrending(@Query("offset") offset: Int): Call<GifListData>

    @GET("gifs/search?api_key=$apiKey&limit=24")
    fun getBySearch(@Query("offset") offset: Int, @Query("q") search: String): Call<GifListData>

    companion object {
        const val apiKey = "tFK8ltPBTPVkWnBQC5AGspOKESkOehq9"
    }
}