package com.miko.gamesss.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.usecase.GamesUseCase

class DetailViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {
    fun getGameDetail(id: String): LiveData<Resource<GameDetail>> =
        gamesUseCase.getDetailGame(id).asLiveData()

    fun setFavoriteState(id: Int, isFavorite: Boolean) {
        gamesUseCase.setFavoriteState(id, isFavorite)
    }

    fun checkGameFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        gamesUseCase.checkFavoriteStatus(id).asLiveData()
}