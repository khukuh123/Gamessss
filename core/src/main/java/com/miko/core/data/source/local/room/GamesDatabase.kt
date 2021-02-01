package com.miko.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miko.core.data.source.local.entity.GameEntity
import com.miko.core.data.source.local.entity.GameFavorite

@Database(entities = [GameEntity::class, GameFavorite::class], version = 1, exportSchema = false)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}