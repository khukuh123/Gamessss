package com.miko.core.domain.usecase

import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import com.miko.core.domain.repository.IGamesRepository
import kotlinx.coroutines.flow.Flow

class GamesInteractor(private val gamesRepository: IGamesRepository) : GamesUseCase {
    override fun getListGame(): Flow<Resource<List<GameList>>> =
        gamesRepository.getListGame()

    override fun searchGame(query: String): Flow<Resource<List<GameList>>> =
        gamesRepository.searchGame(query)

    override fun getDetailGame(id: String): Flow<Resource<GameDetail>> =
        gamesRepository.getDetailGame(id)

    override fun setFavoriteState(id: Int, isFavorite: Boolean) {
        if (isFavorite) {
            gamesRepository.insertFavoriteGame(id)
        } else {
            gamesRepository.deleteFavoriteGame(id)
        }
    }

    override fun checkFavoriteStatus(id: Int): Flow<List<GameDetail>> =
        gamesRepository.checkFavoriteStatus(id)

    override fun getFavoriteGames(): Flow<List<GameList>> =
        gamesRepository.getFavoriteGames()

    override fun deleteFavoriteGame(gameId: Int) =
        gamesRepository.deleteFavoriteGame(gameId)

}