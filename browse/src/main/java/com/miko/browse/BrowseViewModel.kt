package com.miko.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase

class BrowseViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    private var _gameLists: LiveData<Resource<out List<GameList>>>? = null

    fun setGameListSorted(type: Int) {
        _gameLists = gamesUseCase.getGameListSorted(type).asLiveData()
    }

    fun getGameListSorted(): LiveData<Resource<out List<GameList>>>? = _gameLists

}