package com.matttax.giphyapp.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GifAPI {

    @GET("gifs/random?api_key=$apiKey&limit=24")
    fun getRandomGif(): Single<SingleGifData>

    @GET("gifs/trending?api_key=$apiKey&limit=24")
    fun getTrending(@Query("offset") offset: Int): Single<GifListData>

    @GET("gifs/search?api_key=$apiKey&limit=24")
    fun getBySearch(@Query("offset") offset: Int, @Query("q") search: String): Single<GifListData>

    companion object {
        const val apiKey = "tFK8ltPBTPVkWnBQC5AGspOKESkOehq9"
    }

}
