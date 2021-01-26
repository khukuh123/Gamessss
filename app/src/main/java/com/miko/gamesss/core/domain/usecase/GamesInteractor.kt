package com.miko.gamesss.core.domain.usecase

import androidx.lifecycle.LiveData
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.domain.repository.IGamesRepository
import com.miko.gamesss.core.data.Resource

class GamesInteractor (private val gamesRepository: IGamesRepository): GamesUseCase {
    override fun getListGame(): LiveData<Resource<List<GameList>>> =
        gamesRepository.getListGame()

    override fun searchGame(query: String): LiveData<Resource<List<GameList>>> =
        gamesRepository.searchGame(query)

    override fun getDetailGame(id: String): LiveData<Resource<GameDetail>> =
        gamesRepository.getDetailGame(id)

    override fun setFavoriteState(id: Int, isFavorite: Boolean) =
        gamesRepository.setFavoriteState(id, isFavorite)

    override fun checkFavoriteStatus(id: Int): LiveData<List<GameDetail>> =
        gamesRepository.checkFavoriteStatus(id)

    override fun getFavoriteGames(): LiveData<List<GameList>> =
        gamesRepository.getFavoriteGames()

    override fun deleteFavoriteGame(gameId: Int) =
        gamesRepository.deleteFavoriteGame(gameId)

}