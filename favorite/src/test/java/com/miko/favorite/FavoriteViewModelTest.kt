package com.miko.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    private lateinit var favoriteViewModel: FavoriteViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var observer: Observer<List<GameList>>

    @Captor
    private lateinit var captor: ArgumentCaptor<List<GameList>>

    private lateinit var flow: Flow<List<GameList>>

    @Before
    fun setUp() {
        flow = flow {
            emit(DataDummy.generateGameList().toList())
        }

        `when`(gamesUseCase.getFavoriteGames()).thenReturn(flow)
        favoriteViewModel = FavoriteViewModel(gamesUseCase)
    }

    @Test
    fun `getFavoriteGames's gamesRepository called at least once, return non-null and 10 size result and there's a change on observer`() =
        coroutineScope.runBlockingTest {

            favoriteViewModel.getFavoriteGames().observeForever(observer)
            verify(gamesUseCase).getFavoriteGames()

            verify(observer).onChanged(captor.capture())
            assertNotNull(captor.value)
            assertEquals(10, captor.value.size)
        }

}