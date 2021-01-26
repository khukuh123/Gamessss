package com.miko.gamesss.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.domain.usecase.GamesUseCase

class FavoriteViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {
    fun getFavoriteGames(): LiveData<List<GameList>> = gamesUseCase.getFavoriteGames()

    fun deleteFavoriteGame(gameId: Int) {
        gamesUseCase.deleteFavoriteGame(gameId)
    }
}