package com.miko.core.domain.usecase

import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import com.miko.core.domain.repository.IGamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    override fun getGameListSorted(type: Int): Flow<Resource<out List<GameList>>> {
        return when (type) {
            0 -> {
                val currentDate = LocalDate.now()
                val lastMonth = currentDate.minusDays(7)
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val string = "${lastMonth.format(format)},${currentDate.format(format)}"
                gamesRepository.getGameListReleased(string)
            }

            1 -> {
                val currentDate = LocalDate.now()
                val lastMonth = currentDate.minusDays(30)
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val string = "${lastMonth.format(format)},${currentDate.format(format)}"
                gamesRepository.getGameListReleased(string)

            }

            2 -> {
                gamesRepository.getGameListMetaCritic()
            }

            else -> {
                flow<Resource<List<GameList>>> { emit(Resource.Error(null, "Choice is unknown")) }
            }
        }
    }
}