package com.miko.core.data.source.remote

import com.miko.core.data.source.remote.network.ApiResponse
import com.miko.core.data.source.remote.network.ApiService
import com.miko.core.data.source.remote.response.GameDetailResponse
import com.miko.core.data.source.remote.response.GameListResponse
import com.miko.core.utils.DataDummy.makeRemoteFlow
import kotlinx.coroutines.flow.Flow

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getGameDetail(id: String): Flow<ApiResponse<GameDetailResponse>> =
        makeRemoteFlow(apiService.getGameDetail(id))

    suspend fun getListGame(): Flow<ApiResponse<GameListResponse>> =
        makeRemoteFlow(apiService.getGameList())

    suspend fun searchGame(query: String): Flow<ApiResponse<GameListResponse>> =
        makeRemoteFlow(apiService.getGameList(query))

    suspend fun getGameListReleased(date: String): Flow<ApiResponse<GameListResponse>> =
        makeRemoteFlow(apiService.getGameListReleased(date))

    suspend fun getGameListMetaCritic(): Flow<ApiResponse<GameListResponse>> =
        makeRemoteFlow(apiService.getGameListMetaCritic())

}