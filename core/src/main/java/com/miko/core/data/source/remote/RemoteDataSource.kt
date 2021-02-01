package com.miko.core.data.source.remote

import android.util.Log
import com.miko.core.data.source.remote.network.ApiResponse
import com.miko.core.data.source.remote.network.ApiService
import com.miko.core.data.source.remote.response.GameDetailResponse
import com.miko.core.data.source.remote.response.GameListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getGameDetail(id: String): Flow<ApiResponse<GameDetailResponse>> {
        return flow {
            try {
                val response = apiService.getGameDetail(id)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getListGame(): Flow<ApiResponse<GameListResponse>> {
        return flow {
            try {
                val response = apiService.getGameList()
                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchGame(query: String): Flow<ApiResponse<GameListResponse>> {
        return flow {
            try {
                val response = apiService.getGameList(query)
                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}