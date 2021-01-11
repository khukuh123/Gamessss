package com.miko.gamesss

import androidx.lifecycle.LiveData
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.GamesDataSource
import com.miko.gamesss.model.source.remote.RemoteDataSource

class FakeGamesRepository (private val remoteDataSource: RemoteDataSource): GamesDataSource {

    override fun getListGame(): LiveData<List<GameList>> = remoteDataSource.getListGame()
}