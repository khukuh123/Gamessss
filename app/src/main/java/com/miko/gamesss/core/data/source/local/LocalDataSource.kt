package com.miko.gamesss.core.data.source.local

import androidx.lifecycle.LiveData
import com.miko.gamesss.core.data.source.local.entity.GameEntity
import com.miko.gamesss.core.data.source.local.entity.GameFavorite
import com.miko.gamesss.core.data.source.local.room.GamesDao

class LocalDataSource private constructor(private val gamesDao: GamesDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(gamesDao: GamesDao): LocalDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LocalDataSource(gamesDao)
        }

    }

    fun getTopList(): LiveData<List<GameEntity>> =
        gamesDao.getTopList()

    fun searchGame(query: String): LiveData<List<GameEntity>> =
        gamesDao.searchGame("%${query}%")

    fun insertGameEntity(gameEntities: List<GameEntity>) {
        gamesDao.insertGameList(gameEntities)
    }

    fun getGameDetail(id: String): LiveData<GameEntity> =
        gamesDao.getGameDetail(id)

    fun insertGameDetail(gameEntity: GameEntity) {
        gamesDao.insertGameDetail(gameEntity)
    }

    fun insertFavoriteGame(gameFavorite: GameFavorite){
        gamesDao.insertFavoriteGame(gameFavorite)
    }

    fun checkFavoriteStatus(id: Int): LiveData<List<GameFavorite>> =
        gamesDao.checkFavoriteStatus(id)

    fun getFavoriteGames(): LiveData<List<GameEntity>> =
        gamesDao.getFavoriteGames()

    fun deleteFavoriteGame(gameFavorite: GameFavorite) {
        gamesDao.deleteFavoriteGame(gameFavorite)
    }
}