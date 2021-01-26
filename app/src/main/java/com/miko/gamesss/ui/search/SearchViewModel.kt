package com.miko.gamesss.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.data.Resource
import com.miko.gamesss.core.domain.usecase.GamesUseCase

class SearchViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    private lateinit var gameLists: LiveData<Resource<List<GameList>>>

    fun setSearchQuery(query: String) {
        gameLists = gamesUseCase.searchGame(query)
    }

    fun getSearchResult(): LiveData<Resource<List<GameList>>> = gameLists
}