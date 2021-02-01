package com.miko.core.domain.repository

import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import kotlinx.coroutines.flow.Flow

interface IGamesRepository {

    fun getListGame(): Flow<Resource<List<GameList>>>

    fun searchGame(query: String): Flow<Resource<List<GameList>>>

    fun getDetailGame(id: String): Flow<Resource<GameDetail>>

    fun insertFavoriteGame(id: Int)

    fun checkFavoriteStatus(id: Int): Flow<List<GameDetail>>

    fun getFavoriteGames(): Flow<List<GameList>>

    fun deleteFavoriteGame(gameId: Int)

}