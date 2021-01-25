package com.miko.gamesss.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.vo.Resource

class DetailViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    fun getGameDetail(id: String): LiveData<Resource<GameDetail>> =
        gamesRepository.getDetailGame(id)

    fun setFavoriteState(id: Int, isFavorite: Boolean) {
        gamesRepository.setFavoriteState(id, isFavorite)
    }

    fun checkGameFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        gamesRepository.checkFavoriteStatus(id)
}