package com.miko.gamesss.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.GamesRepository

class FavoriteViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    fun getFavoriteGames(): LiveData<List<GameList>> = gamesRepository.getFavoriteGames()

    fun deleteFavoriteGame(gameId: Int) {
        gamesRepository.deleteFavoriteGame(gameId)
    }
}