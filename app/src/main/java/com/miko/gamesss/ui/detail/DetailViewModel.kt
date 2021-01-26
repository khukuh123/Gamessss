package com.miko.gamesss.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.data.Resource
import com.miko.gamesss.core.domain.usecase.GamesUseCase

class DetailViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {
    fun getGameDetail(id: String): LiveData<Resource<GameDetail>> =
        gamesUseCase.getDetailGame(id)

    fun setFavoriteState(id: Int, isFavorite: Boolean) {
        gamesUseCase.setFavoriteState(id, isFavorite)
    }

    fun checkGameFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        gamesUseCase.checkFavoriteStatus(id)
}