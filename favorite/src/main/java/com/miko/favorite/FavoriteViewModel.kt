package com.miko.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase

class FavoriteViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {
    fun getFavoriteGames(): LiveData<List<GameList>> = gamesUseCase.getFavoriteGames().asLiveData()

    fun deleteFavoriteGame(gameId: Int) {
        gamesUseCase.deleteFavoriteGame(gameId)
    }
}