package com.miko.core.data

import com.miko.core.data.source.local.LocalDataSource
import com.miko.core.data.source.remote.RemoteDataSource
import com.miko.core.data.source.remote.network.ApiResponse
import com.miko.core.data.source.remote.response.GameDetailResponse
import com.miko.core.data.source.remote.response.GameListResponse
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import com.miko.core.domain.repository.IGamesRepository
import com.miko.core.utils.AppExecutors
import com.miko.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GamesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IGamesRepository {

    override fun getListGame(): Flow<Resource<List<GameList>>> {
        return object : NetworkBoundResource<List<GameList>, GameListResponse>() {
            override fun loadFromDB(): Flow<List<GameList>> =
                localDataSource.getTopList().map {
                    DataMapper.fromGameEntitiesToGameLists(it)
                }

            override fun shouldFetch(data: List<GameList>?): Boolean =
                data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<GameListResponse>> =
                remoteDataSource.getListGame()

            override suspend fun saveCallResult(data: GameListResponse) =
                localDataSource.insertGameEntity(DataMapper.fromGameListResponseToGameEntities(data))

        }.asFlow()
    }

    override fun searchGame(query: String): Flow<Resource<List<GameList>>> {
        return object : NetworkBoundResource<List<GameList>, GameListResponse>() {
            override fun loadFromDB(): Flow<List<GameList>> =
                localDataSource.searchGame(query).map {
                    DataMapper.fromGameEntitiesToGameLists(it)
                }

            override fun shouldFetch(data: List<GameList>?): Boolean {
                val dataSize = data?.size ?: 0
                return data.isNullOrEmpty() || dataSize < 10
            }

            override suspend fun createCall(): Flow<ApiResponse<GameListResponse>> =
                remoteDataSource.searchGame(query)

            override suspend fun saveCallResult(data: GameListResponse) =
                localDataSource.insertGameEntity(DataMapper.fromGameListResponseToGameEntities(data))

        }.asFlow()
    }

    override fun getDetailGame(id: String): Flow<Resource<GameDetail>> {
        return object : NetworkBoundResource<GameDetail, GameDetailResponse>() {
            override fun loadFromDB(): Flow<GameDetail> =
                localDataSource.getGameDetail(id).map {
                    DataMapper.fromGameEntityToGameDetail(it)
                }

            override fun shouldFetch(data: GameDetail?): Boolean =
                data?.description?.isEmpty() as Boolean

            override suspend fun createCall(): Flow<ApiResponse<GameDetailResponse>> =
                remoteDataSource.getGameDetail(id)

            override suspend fun saveCallResult(data: GameDetailResponse) =
                localDataSource.insertGameDetail(DataMapper.fromGameDetailResponseToGameEntity(data))

        }.asFlow()
    }

    override fun insertFavoriteGame(id: Int) {
        val gameFavorite = DataMapper.fromGameIdToFavoriteGame(id)
        appExecutors.diskIO().execute {
            localDataSource.insertFavoriteGame(gameFavorite)
        }
    }

    override fun checkFavoriteStatus(id: Int): Flow<List<GameDetail>> =
        localDataSource.checkFavoriteStatus(id).map {
            DataMapper.fromGameFavoritesToGameDetails(it)
        }

    override fun getFavoriteGames(): Flow<List<GameList>> =
        localDataSource.getFavoriteGames().map {
            DataMapper.fromGameEntitiesToGameLists(it)
        }

    override fun deleteFavoriteGame(gameId: Int) {
        val gameFavorite = DataMapper.fromGameIdToFavoriteGame(gameId)
        appExecutors.diskIO().execute {
            localDataSource.deleteFavoriteGame(gameFavorite)
        }
    }
}