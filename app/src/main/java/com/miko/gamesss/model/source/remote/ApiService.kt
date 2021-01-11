package com.miko.gamesss.model.source.remote

import com.miko.gamesss.model.source.remote.response.GameListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("games?page_size=10")
    fun getGameList(): Call<GameListResponse>

    @GET("games")
    fun getGameList(@Query("search") query: String): Call<GameListResponse>
}