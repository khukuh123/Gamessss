package com.miko.gamesss.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase

class SearchViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    private lateinit var gameLists: LiveData<Resource<List<GameList>>>

    fun setSearchQuery(query: String) {
        gameLists = gamesUseCase.searchGame(query).asLiveData()
    }

    fun getSearchResult(): LiveData<Resource<List<GameList>>> =
        gameLists
}