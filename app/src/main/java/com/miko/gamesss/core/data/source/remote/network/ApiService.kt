package com.miko.gamesss.core.data.source.remote.network

import com.miko.gamesss.core.data.source.remote.response.GameDetailResponse
import com.miko.gamesss.core.data.source.remote.response.GameListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("games?page_size=10")
    fun getGameList(): Call<GameListResponse>

    @GET("games")
    fun getGameList(@Query("search") query: String): Call<GameListResponse>

    @GET("games/{id}")
    fun getGameDetail(@Path("id") id: String): Call<GameDetailResponse>
}