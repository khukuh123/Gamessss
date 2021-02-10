package com.miko.core.data.source.local.room

import androidx.room.*
import com.miko.core.data.source.local.entity.GameEntity
import com.miko.core.data.source.local.entity.GameFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    @Query("SELECT * FROM gameEntities ORDER BY metaCritic DESC LIMIT 10")
    fun getTopList(): Flow<List<GameEntity>>

    @Query("SELECT * FROM gameEntities WHERE name LIKE :query")
    fun searchGame(query: String): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameList(gameLists: List<GameEntity>)

    @Query("SELECT * FROM gameEntities WHERE gameId = :id")
    fun getGameDetail(id: String): Flow<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetail(gameDetail: GameEntity)

    @Query("SELECT * FROM gameEntities, gamesFavorite WHERE gameEntities.gameId = gamesFavorite.gameId")
    fun getFavoriteGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteGame(gameFavorite: GameFavorite)

    @Delete
    fun deleteFavoriteGame(gameFavorite: GameFavorite)

    @Query("SELECT * FROM gamesFavorite WHERE gameId = :id")
    fun checkFavoriteStatus(id: Int): Flow<List<GameFavorite>>

}