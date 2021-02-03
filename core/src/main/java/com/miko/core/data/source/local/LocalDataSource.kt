package com.miko.core.data.source.local

import com.miko.core.data.source.local.entity.GameEntity
import com.miko.core.data.source.local.entity.GameFavorite
import com.miko.core.data.source.local.room.GamesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take

class LocalDataSource(private val gamesDao: GamesDao) {

    fun getTopList(): Flow<List<GameEntity>> =
        gamesDao.getTopList()

    fun searchGame(query: String): Flow<List<GameEntity>> =
        gamesDao.searchGame("%${query}%")

    suspend fun insertGameEntity(gameEntities: List<GameEntity>) {
        gamesDao.insertGameList(gameEntities)
    }

    fun getGameDetail(id: String): Flow<GameEntity> =
        gamesDao.getGameDetail(id)

    suspend fun insertGameDetail(gameEntity: GameEntity) {
        gamesDao.insertGameDetail(gameEntity)
    }

    fun insertFavoriteGame(gameFavorite: GameFavorite) {
        gamesDao.insertFavoriteGame(gameFavorite)
    }

    fun checkFavoriteStatus(id: Int): Flow<List<GameFavorite>> =
        gamesDao.checkFavoriteStatus(id).take(1)

    fun getFavoriteGames(): Flow<List<GameEntity>> =
        gamesDao.getFavoriteGames()

    fun deleteFavoriteGame(gameFavorite: GameFavorite) {
        gamesDao.deleteFavoriteGame(gameFavorite)
    }

}