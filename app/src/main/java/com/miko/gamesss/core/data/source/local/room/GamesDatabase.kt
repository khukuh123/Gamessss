package com.miko.gamesss.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miko.gamesss.core.data.source.local.entity.GameEntity
import com.miko.gamesss.core.data.source.local.entity.GameFavorite

@Database(entities = [GameEntity::class, GameFavorite::class], version = 1, exportSchema = false)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao

    companion object {
        @Volatile
        private var INSTANCE: GamesDatabase? = null

        fun getInstance(context: Context): GamesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                GamesDatabase::class.java, "Gamesss.db"
            ).build()
        }
    }
}