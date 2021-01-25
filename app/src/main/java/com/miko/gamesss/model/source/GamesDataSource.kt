package com.miko.gamesss.model.source

import androidx.lifecycle.LiveData
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.GameList
import com.miko.gamesss.vo.Resource

interface GamesDataSource {

    fun getListGame(): LiveData<Resource<List<GameList>>>

    fun searchGame(query: String): LiveData<Resource<List<GameList>>>

    fun getDetailGame(id: String): LiveData<Resource<GameDetail>>

    fun setFavoriteState(id: Int, isFavorite: Boolean)

    fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>>

    fun getFavoriteGames(): LiveData<List<GameList>>

    fun deleteFavoriteGame(gameId: Int)
}