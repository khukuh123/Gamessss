package com.miko.gamesss.core.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.miko.gamesss.core.data.NetworkBoundResource
import com.miko.gamesss.core.data.source.local.LocalDataSource
import com.miko.gamesss.core.data.source.remote.RemoteDataSource
import com.miko.gamesss.core.data.source.remote.network.ApiResponse
import com.miko.gamesss.core.data.source.remote.response.GameDetailResponse
import com.miko.gamesss.core.data.source.remote.response.GameListResponse
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.domain.repository.IGamesRepository
import com.miko.gamesss.core.utils.AppExecutors
import com.miko.gamesss.core.utils.DataMapper
import com.miko.gamesss.core.data.Resource

class FakeGamesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IGamesRepository {

    override fun getListGame(): LiveData<Resource<List<GameList>>> {
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<GameList>> =
                Transformations.map(localDataSource.getTopList()) {
                    DataMapper.fromGameEntitiesToGameLists(it)
                }

            override fun shouldFetch(data: List<GameList>?): Boolean {
                return data.isNullOrEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<GameListResponse>> =
                remoteDataSource.getListGame()

            override fun saveCallResult(data: GameListResponse) {
                localDataSource.insertGameEntity(DataMapper.fromGameListResponseToGameEntities(data))
            }

        }.asLiveData()
    }

    override fun searchGame(query: String): LiveData<Resource<List<GameList>>> {
        return object : NetworkBoundResource<List<GameList>, GameListResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<GameList>> =
                Transformations.map(localDataSource.searchGame(query)) {
                    DataMapper.fromGameEntitiesToGameLists(it)
                }

            override fun shouldFetch(data: List<GameList>?): Boolean {
                val dataSize = data?.size ?: 0
                return data.isNullOrEmpty() || dataSize < 10
            }

            override fun createCall(): LiveData<ApiResponse<GameListResponse>> =
                remoteDataSource.searchGame(query)

            override fun saveCallResult(data: GameListResponse) {
                localDataSource.insertGameEntity(DataMapper.fromGameListResponseToGameEntities(data))
            }

        }.asLiveData()
    }

    override fun getDetailGame(id: String): LiveData<Resource<GameDetail>> {
        return object : NetworkBoundResource<GameDetail, GameDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<GameDetail> =
                Transformations.map(localDataSource.getGameDetail(id)) {
                    DataMapper.fromGameEntityToGameDetail(it)
                }

            override fun shouldFetch(data: GameDetail?): Boolean {
                return data?.description?.isEmpty() as Boolean
            }

            override fun createCall(): LiveData<ApiResponse<GameDetailResponse>> =
                remoteDataSource.getGameDetail(id)

            override fun saveCallResult(data: GameDetailResponse) {
                localDataSource.insertGameDetail(DataMapper.fromGameDetailResponseToGameEntity(data))
            }
        }.asLiveData()
    }

    override fun setFavoriteState(id: Int, isFavorite: Boolean) {
        appExecutors.diskIO().execute {
            localDataSource.setFavoriteState(id, isFavorite)
        }
    }

    override fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        Transformations.map(localDataSource.checkFavoriteStatus(id)) {
            DataMapper.fromGameFavoritesToGameDetails(it)
        }

    override fun getFavoriteGames(): LiveData<List<GameList>> =
        Transformations.map(localDataSource.getFavoriteGames()) {
            DataMapper.fromGameEntitiesToGameLists(it)
        }

    override fun deleteFavoriteGame(gameId: Int) {
        appExecutors.diskIO().execute {
            localDataSource.deleteFavoriteGame(gameId)
        }
    }
}