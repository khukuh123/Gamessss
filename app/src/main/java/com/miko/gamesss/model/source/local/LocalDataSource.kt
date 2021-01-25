package com.miko.gamesss.model.source.local

import androidx.lifecycle.LiveData
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.local.entity.GameEntity
import com.miko.gamesss.model.source.local.room.GamesDao
import com.miko.gamesss.utils.RoomEntity

class LocalDataSource private constructor(private val gamesDao: GamesDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(gamesDao: GamesDao): LocalDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LocalDataSource(gamesDao)
        }

    }

    fun getTopList(): LiveData<List<GameList>> =
        RoomEntity.fromGameEntitiesToGameLists(gamesDao.getTopList())

    fun searchGame(query: String) =
        RoomEntity.fromGameEntitiesToGameLists(gamesDao.searchGame("%${query}%"))

    fun insertGameEntity(gameEntities: List<GameEntity>) {
        gamesDao.insertGameList(gameEntities)
    }

    fun getGameDetail(id: String): LiveData<GameDetail> =
        RoomEntity.fromGameEntityToGameDetail(gamesDao.getGameDetail(id))

    fun insertGameDetail(gameEntity: GameEntity) {
        gamesDao.insertGameDetail(gameEntity)
    }

    fun setFavoriteState(id: Int, isFavorite: Boolean) {
        if (isFavorite) {
            gamesDao.insertFavoriteGame(RoomEntity.fromGameIdToFavoriteGame(id))
        } else {
            gamesDao.deleteFavoriteGame(RoomEntity.fromGameIdToFavoriteGame(id))
        }
    }

    fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        RoomEntity.checkFavoriteStatus(gamesDao.checkFavoriteStatus(id))

    fun getFavoriteGames(): LiveData<List<GameList>> =
        RoomEntity.fromGameEntitiesToGameLists(gamesDao.getFavoriteGames())

    fun deleteFavoriteGame(gameId: Int) {
        gamesDao.deleteFavoriteGame(RoomEntity.fromGameIdToFavoriteGame(gameId))
    }
}