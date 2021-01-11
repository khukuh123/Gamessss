package com.miko.gamesss.model.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miko.gamesss.model.source.local.entity.GameEntity

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class GamesDatabase: RoomDatabase() {
    abstract fun gamesDao() : GamesDao

    companion object{
        @Volatile
        private var INSTANCE : GamesDatabase? = null

        fun getInstance(context: Context): GamesDatabase = INSTANCE ?: synchronized(this){
            INSTANCE ?: Room.databaseBuilder(context.applicationContext,
            GamesDatabase::class.java, "Gamesss.db").build()
        }
    }
}