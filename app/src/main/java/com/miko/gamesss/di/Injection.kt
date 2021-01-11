package com.miko.gamesss.di

import android.content.Context
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.model.source.local.LocalDataSource
import com.miko.gamesss.model.source.local.room.GamesDatabase
import com.miko.gamesss.model.source.remote.RemoteDataSource
import com.miko.gamesss.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): GamesRepository {
        val database = GamesDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.gamesDao())
        val appExecutors = AppExecutors()
        return GamesRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

}