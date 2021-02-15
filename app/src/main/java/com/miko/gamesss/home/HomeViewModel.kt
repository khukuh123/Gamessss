package com.miko.gamesss.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase

class HomeViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    fun getGameList(): LiveData<Resource<List<GameList>>> = gamesUseCase.getListGame().asLiveData()

}