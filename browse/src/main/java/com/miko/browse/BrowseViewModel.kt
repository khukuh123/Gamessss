package com.miko.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase

class BrowseViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    fun getGameListSorted(type: Int): LiveData<Resource<out List<GameList>>> =
        gamesUseCase.getGameListSorted(type).asLiveData()

}