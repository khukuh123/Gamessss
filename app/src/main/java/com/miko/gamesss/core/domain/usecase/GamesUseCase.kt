package com.miko.gamesss.core.domain.usecase

import androidx.lifecycle.LiveData
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.data.Resource

interface GamesUseCase {

    fun getListGame(): LiveData<Resource<List<GameList>>>

    fun searchGame(query: String): LiveData<Resource<List<GameList>>>

    fun getDetailGame(id: String): LiveData<Resource<GameDetail>>

    fun setFavoriteState(id: Int, isFavorite: Boolean)

    fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>>

    fun getFavoriteGames(): LiveData<List<GameList>>

    fun deleteFavoriteGame(gameId: Int)

}