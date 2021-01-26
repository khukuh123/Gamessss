package com.miko.gamesss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.data.Resource
import com.miko.gamesss.core.domain.usecase.GamesUseCase

class HomeViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    private lateinit var gameLists: LiveData<Resource<List<GameList>>>

    fun setGameList() {
        gameLists = gamesUseCase.getListGame()
    }

    fun getGameList(): LiveData<Resource<List<GameList>>> = gameLists

}