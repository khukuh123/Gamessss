package com.miko.core.domain.usecase

import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import kotlinx.coroutines.flow.Flow

interface GamesUseCase {

    fun getListGame(): Flow<Resource<List<GameList>>>

    fun searchGame(query: String): Flow<Resource<List<GameList>>>

    fun getDetailGame(id: String): Flow<Resource<GameDetail>>

    fun checkFavoriteStatus(id: Int): Flow<List<GameDetail>>

    fun getFavoriteGames(): Flow<List<GameList>>

    fun deleteFavoriteGame(gameId: Int)

    fun setFavoriteState(id: Int, isFavorite: Boolean)
}