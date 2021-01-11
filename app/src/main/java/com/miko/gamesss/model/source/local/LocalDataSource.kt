package com.miko.gamesss.model.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    fun getTopList(): LiveData<List<GameList>> = RoomEntity.toGameLists(gamesDao.getTopList())

    fun searchGame(query: String) = RoomEntity.toGameLists(gamesDao.searchGame("%${query}%"))

    fun insertGameEntity(gameEntities: List<GameEntity>){
        gamesDao.insertGameList(gameEntities)
    }

}