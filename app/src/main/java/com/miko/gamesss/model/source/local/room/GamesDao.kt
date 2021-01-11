package com.miko.gamesss.model.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miko.gamesss.model.source.local.entity.GameEntity

@Dao
interface GamesDao {

    @Query("SELECT * FROM gameentities")
    fun getTopList(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM gameentities WHERE name LIKE :query")
    fun searchGame(query: String): LiveData<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameList(gameLists: List<GameEntity>)

}