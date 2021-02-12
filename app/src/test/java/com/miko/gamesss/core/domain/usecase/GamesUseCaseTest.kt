package com.miko.gamesss.core.domain.usecase

import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import com.miko.core.domain.repository.IGamesRepository
import com.miko.core.domain.usecase.GamesInteractor
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.core.utils.DataDummy
import com.miko.gamesss.MainCoroutineScopeRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GamesUseCaseTest {

    private lateinit var gamesUseCase: GamesUseCase
    private lateinit var gameLists: Flow<Resource<List<GameList>>>
    private lateinit var gameDetail: Flow<Resource<GameDetail>>
    private lateinit var gameDetails: Flow<List<GameDetail>>
    private lateinit var gameFavorites: Flow<List<GameList>>

    @Mock
    private lateinit var gamesRepository: IGamesRepository

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private lateinit var dates: String

    companion object {
        const val GAME_ID = 12
        const val GAME_NAME = "Portal"
        const val LAST_WEEK = 0
        const val METACRITIC = 2
    }

    @Before
    fun setUp() {
        val currentDate = LocalDate.now()
        val lastMonth = currentDate.minusDays(7)
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val string = "${lastMonth.format(format)},${currentDate.format(format)}"
        dates = string

        gamesUseCase = GamesInteractor(gamesRepository)

        gameLists = flow {
            emit(Resource.Loading(listOf()))
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesRepository.getListGame()).thenReturn(gameLists)
        `when`(gamesRepository.searchGame(GAME_NAME)).thenReturn(gameLists)
        `when`(gamesRepository.getGameListMetaCritic()).thenReturn(gameLists)
        `when`(gamesRepository.getGameListReleased(dates)).thenReturn(gameLists)

        gameDetail = flow {
            emit(Resource.Loading(GameDetail()))
            emit(Resource.Success(DataDummy.generateGameDetail()))
        }
        `when`(gamesRepository.getDetailGame(GAME_ID.toString())).thenReturn(gameDetail)

        gameDetails = flow {
            emit(listOf(DataDummy.generateGameDetail()))
        }
        `when`(gamesRepository.checkFavoriteStatus(GAME_ID)).thenReturn(gameDetails)

        gameFavorites = flow {
            emit(DataDummy.generateGameList())
        }
        `when`(gamesRepository.getFavoriteGames()).thenReturn(gameFavorites)
    }

    @Test
    fun getListGame() = coroutineScope.runBlockingTest {
        gamesUseCase.getListGame().collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
                assertEquals(10, value.data?.size)
            }
        }

        verify(gamesRepository).getListGame()
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun searchGame() = coroutineScope.runBlockingTest {
        gamesUseCase.searchGame(GAME_NAME).collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
                assertEquals(10, value.data?.size)
            }
        }


        verify(gamesRepository).searchGame(GAME_NAME)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun getDetailGame() = coroutineScope.runBlockingTest {
        gamesUseCase.getDetailGame(GAME_ID.toString()).collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
            }
        }

        verify(gamesRepository).getDetailGame(GAME_ID.toString())
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun setFavoriteState() {
        gamesUseCase.setFavoriteState(GAME_ID, true)
        verify(gamesRepository).insertFavoriteGame(GAME_ID)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun checkFavoriteStatus() = coroutineScope.runBlockingTest {
        val result = gamesUseCase.checkFavoriteStatus(GAME_ID).first()
        verify(gamesRepository).checkFavoriteStatus(GAME_ID)
        assertNotNull(result)
        assertEquals(1, result.size)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun getFavoriteGames() = coroutineScope.runBlockingTest {
        val result = gamesUseCase.getFavoriteGames().first()

        verify(gamesRepository).getFavoriteGames()
        assertNotNull(result)
        assertEquals(10, result.size)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun deleteFavoriteGame() {
        gamesUseCase.deleteFavoriteGame(GAME_ID)
        verify(gamesRepository).deleteFavoriteGame(GAME_ID)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun getGameListSorted() = coroutineScope.runBlockingTest {
        gamesUseCase.getGameListSorted(LAST_WEEK).collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)

                assertNotNull(value)
                assertEquals(10, value.data?.size)
            }
        }
        gamesUseCase.getGameListSorted(METACRITIC).collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)

                assertNotNull(value)
                assertEquals(10, value.data?.size)
            }
        }

        verify(gamesRepository).getGameListMetaCritic()
        verify(gamesRepository).getGameListReleased(dates)

        verifyNoMoreInteractions(gamesRepository)
    }
}