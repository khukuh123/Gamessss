package com.miko.gamesss.model.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.miko.gamesss.model.source.local.entity.GameEntity
import com.miko.gamesss.model.source.local.entity.GameFavorite

@Dao
interface GamesDao {

    @Query("SELECT * FROM gameEntities ORDER BY rating DESC LIMIT 10")
    fun getTopList(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM gameEntities WHERE name LIKE :query")
    fun searchGame(query: String): LiveData<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameList(gameLists: List<GameEntity>)

    @Query("SELECT * FROM gameEntities WHERE gameId = :id")
    fun getGameDetail(id: String): LiveData<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameDetail(gameDetail: GameEntity)

    @Query("SELECT * FROM gameEntities, gamesFavorite WHERE gameEntities.gameId = gamesFavorite.gameId")
    fun getFavoriteGames(): LiveData<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteGame(gameFavorite: GameFavorite)

    @Delete
    fun deleteFavoriteGame(gameFavorite: GameFavorite)

    @Query("SELECT * FROM gamesFavorite WHERE gameId = :id")
    fun checkFavoriteStatus(id: Int): LiveData<List<GameFavorite>>
}