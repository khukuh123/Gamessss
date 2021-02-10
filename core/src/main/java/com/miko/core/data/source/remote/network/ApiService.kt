package com.miko.core.data.source.remote.network

import com.miko.core.data.source.remote.response.GameDetailResponse
import com.miko.core.data.source.remote.response.GameListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("games?page_size=10")
    suspend fun getGameList(): GameListResponse

    @GET("games")
    suspend fun getGameList(@Query("search") query: String): GameListResponse

    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") id: String): GameDetailResponse

    @GET("games")
    suspend fun getGameListReleased(@Query("dates") date: String): GameListResponse

    @GET("games?ordering=-metacritic")
    suspend fun getGameListMetaCritic(): GameListResponse

}