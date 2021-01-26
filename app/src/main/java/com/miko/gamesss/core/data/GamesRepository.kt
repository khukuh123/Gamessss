package com.miko.gamesss.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.data.source.local.LocalDataSource
import com.miko.gamesss.core.data.source.remote.network.ApiResponse
import com.miko.gamesss.core.data.source.remote.RemoteDataSource
import com.miko.gamesss.core.data.source.remote.response.GameDetailResponse
import com.miko.gamesss.core.data.source.remote.response.GameListResponse
import com.miko.gamesss.core.domain.repository.IGamesRepository
import com.miko.gamesss.core.utils.AppExecutors
import com.miko.gamesss.core.utils.EspressoIdlingResource
import com.miko.gamesss.core.utils.DataMapper

class GamesRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IGamesRepository {

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
                Transformations.map(localDataSource.getTopList()){
                    DataMapper.fromGameEntitiesToGameLists(it)
                }

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
                localDataSource.insertGameEntity(DataMapper.fromGameListResponseToGameEntities(data))
                EspressoIdlingResource.decrement()
            }

        }.asLiveData()
    }

    override fun searchGame(query: String): LiveData<Resource<List<GameList>>> {
        EspressoIdlingResource.increment()
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<GameList>> =
                Transformations.map(localDataSource.searchGame(query)){
                    DataMapper.fromGameEntitiesToGameLists(it)
                }

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
                localDataSource.insertGameEntity(DataMapper.fromGameListResponseToGameEntities(data))
                EspressoIdlingResource.decrement()
            }

        }.asLiveData()
    }

    override fun getDetailGame(id: String): LiveData<Resource<GameDetail>> {
        EspressoIdlingResource.increment()
        return object : NetworkBoundResource<GameDetail, GameDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<GameDetail> =
                Transformations.map(localDataSource.getGameDetail(id)){
                    DataMapper.fromGameEntityToGameDetail(it)
                }

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
                localDataSource.insertGameDetail(DataMapper.fromGameDetailResponseToGameEntity(data))
                EspressoIdlingResource.decrement()
            }
        }.asLiveData()
    }

    override fun setFavoriteState(id: Int, isFavorite: Boolean) {
        val gameFavorite = DataMapper.fromGameIdToFavoriteGame(id)
        appExecutors.diskIO().execute {
            if(isFavorite){
                localDataSource.insertFavoriteGame(gameFavorite)
            }else{
                localDataSource.deleteFavoriteGame(gameFavorite)
            }
        }
    }

    override fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        Transformations.map(localDataSource.checkFavoriteStatus(id)){
            DataMapper.fromGameFavoritesToGameDetails(it)
        }

    override fun getFavoriteGames(): LiveData<List<GameList>> =
        Transformations.map(localDataSource.getFavoriteGames()){
            DataMapper.fromGameEntitiesToGameLists(it)
        }

    override fun deleteFavoriteGame(gameId: Int) {
        val gameFavorite = DataMapper.fromGameIdToFavoriteGame(gameId)
        appExecutors.diskIO().execute {
            localDataSource.deleteFavoriteGame(gameFavorite)
        }
    }
}