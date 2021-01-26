package com.miko.gamesss.core.di

import android.content.Context
import com.miko.gamesss.core.data.GamesRepository
import com.miko.gamesss.core.data.source.local.LocalDataSource
import com.miko.gamesss.core.data.source.local.room.GamesDatabase
import com.miko.gamesss.core.data.source.remote.RemoteDataSource
import com.miko.gamesss.core.domain.usecase.GamesInteractor
import com.miko.gamesss.core.domain.usecase.GamesUseCase
import com.miko.gamesss.core.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): GamesRepository {
        val database = GamesDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.gamesDao())
        val appExecutors = AppExecutors()
        return GamesRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideGamesUseCase(context: Context): GamesUseCase{
        val repository = provideRepository(context)
        return GamesInteractor(repository)
    }

}