package com.miko.gamesss.model.source

import androidx.lifecycle.LiveData
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.local.LocalDataSource
import com.miko.gamesss.model.source.remote.ApiResponse
import com.miko.gamesss.model.source.remote.RemoteDataSource
import com.miko.gamesss.model.source.remote.response.GameDetailResponse
import com.miko.gamesss.model.source.remote.response.GameListResponse
import com.miko.gamesss.utils.AppExecutors
import com.miko.gamesss.utils.EspressoIdlingResource
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

    override fun getListGame(): LiveData<Resource<List<GameList>>> {
        EspressoIdlingResource.increment()
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<GameList>> =
                localDataSource.getTopList()

            override fun shouldFetch(data: List<GameList>?): Boolean {
                return if (data.isNullOrEmpty()) {
                    true
                } else {
                    EspressoIdlingResource.decrement()
                    false
                }
            }

            override fun createCall(): LiveData<ApiResponse<GameListResponse>> =
                remoteDataSource.getListGame()

            override fun saveCallResult(data: GameListResponse) {
                localDataSource.insertGameEntity(RoomEntity.fromGameListResponseToGameEntities(data))
                EspressoIdlingResource.decrement()
            }

        }.asLiveData()
    }

    override fun searchGame(query: String): LiveData<Resource<List<GameList>>> {
        EspressoIdlingResource.increment()
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<GameList>> =
                localDataSource.searchGame(query)

            override fun shouldFetch(data: List<GameList>?): Boolean {
                val dataSize = data?.size ?: 0
                return if (data.isNullOrEmpty() || dataSize < 10) {
                    true
                } else {
                    EspressoIdlingResource.decrement()
                    false
                }
            }

            override fun createCall(): LiveData<ApiResponse<GameListResponse>> =
                remoteDataSource.searchGame(query)

            override fun saveCallResult(data: GameListResponse) {
                localDataSource.insertGameEntity(RoomEntity.fromGameListResponseToGameEntities(data))
                EspressoIdlingResource.decrement()
            }

        }.asLiveData()
    }

    override fun getDetailGame(id: String): LiveData<Resource<GameDetail>> {
        EspressoIdlingResource.increment()
        return object : NetworkBoundResource<GameDetail, GameDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<GameDetail> =
                localDataSource.getGameDetail(id)

            override fun shouldFetch(data: GameDetail?): Boolean {
                return if (data?.description?.isEmpty() as Boolean) {
                    true
                } else {
                    EspressoIdlingResource.decrement()
                    false
                }
            }

            override fun createCall(): LiveData<ApiResponse<GameDetailResponse>> =
                remoteDataSource.getGameDetail(id)

            override fun saveCallResult(data: GameDetailResponse) {
                localDataSource.insertGameDetail(RoomEntity.fromGameDetailResponseToGameEntity(data))
                EspressoIdlingResource.decrement()
            }
        }.asLiveData()
    }

    override fun setFavoriteState(id: Int, isFavorite: Boolean) {
        appExecutors.diskIO().execute {
            localDataSource.setFavoriteState(id, isFavorite)
        }
    }

    override fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        localDataSource.checkFavoriteStatus(id)

    override fun getFavoriteGames(): LiveData<List<GameList>> = localDataSource.getFavoriteGames()

    override fun deleteFavoriteGame(gameId: Int) {
        appExecutors.diskIO().execute {
            localDataSource.deleteFavoriteGame(gameId)
        }
    }
}