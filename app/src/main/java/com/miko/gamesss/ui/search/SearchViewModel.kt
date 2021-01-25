package com.miko.gamesss.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.vo.Resource

class SearchViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    private lateinit var gameLists: LiveData<Resource<List<GameList>>>

    fun setSearchQuery(query: String) {
        gameLists = gamesRepository.searchGame(query)
    }

    fun getSearchResult(): LiveData<Resource<List<GameList>>> = gameLists
}