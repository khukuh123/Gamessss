package com.miko.gamesss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.vo.Resource

class HomeViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    private lateinit var gameLists: LiveData<Resource<List<GameList>>>

    fun setGameList() {
        gameLists = gamesRepository.getListGame()
    }

    fun getGameList(): LiveData<Resource<List<GameList>>> = gameLists

}