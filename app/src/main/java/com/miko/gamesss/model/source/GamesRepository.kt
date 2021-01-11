package com.miko.gamesss.model.source

import androidx.lifecycle.LiveData
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.local.LocalDataSource
import com.miko.gamesss.model.source.remote.ApiResponse
import com.miko.gamesss.model.source.remote.RemoteDataSource
import com.miko.gamesss.model.source.remote.response.GameListResponse
import com.miko.gamesss.utils.AppExecutors
import com.miko.gamesss.utils.RoomEntity
import com.miko.gamesss.vo.Resource

class GamesRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : GamesDataSource {

    companion object {
        @Volatile
        private var instance: GamesRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): GamesRepository = instance ?: synchronized(this) {
            instance ?: GamesRepository(
                remoteDataSource,
                localDataSource,
                appExecutors
            )
        }
    }

    override fun getListGame(): LiveData<Resource<List<GameList>>>{
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors){
            override fun loadFromDB(): LiveData<List<GameList>> =
                localDataSource.getTopList()

            override fun shouldFetch(data: List<GameList>?): Boolean =
                data.isNullOrEmpty()

            override fun createCall(): LiveData<ApiResponse<GameListResponse>> =
                remoteDataSource.getListGame()

            override fun saveCallResult(data: GameListResponse) {
                localDataSource.insertGameEntity(RoomEntity.fromGameListResponse(data))
            }

        }.asLiveData()
    }

    override fun searchGame(query: String): LiveData<Resource<List<GameList>>> {
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors){
            override fun loadFromDB(): LiveData<List<GameList>> =
                localDataSource.searchGame(query)

            override fun shouldFetch(data: List<GameList>?): Boolean =
                data.isNullOrEmpty()

            override fun createCall(): LiveData<ApiResponse<GameListResponse>> =
                remoteDataSource.searchGame(query)

            override fun saveCallResult(data: GameListResponse) {
                localDataSource.insertGameEntity(RoomEntity.fromGameListResponse(data))
            }

        }.asLiveData()
    }
}