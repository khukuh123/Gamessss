package com.miko.gamesss.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.core.utils.DataDummy
import com.miko.gamesss.MainCoroutineScopeRule
import com.nhaarman.mockitokotlin2.atLeast
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
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var observer: Observer<Resource<List<GameList>>>

    @Captor
    private lateinit var captor: ArgumentCaptor<Resource<List<GameList>>>

    private lateinit var flow: Flow<Resource<List<GameList>>>

    @Before
    fun setUp() {
        flow = flow {
            emit(Resource.Loading(listOf()))
            kotlinx.coroutines.delay(10)
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesUseCase.getListGame()).thenReturn(flow)

        homeViewModel = HomeViewModel(gamesUseCase)
    }

    @Test
    fun `getGameList's gamesRepository called at least once, return non-null and 10 size result and there's a change on observer`() =
        coroutineScope.runBlockingTest {
            homeViewModel.setGameList()
            homeViewModel.getGameList().observeForever(observer)
            verify(gamesUseCase).getListGame()

            verify(observer).onChanged(captor.capture())

            coroutineScope.advanceTimeBy(10)

            verify(observer, atLeast(2)).onChanged(captor.capture())
            assertNotNull(captor.value)
            assertEquals(10, captor.value.data?.size)
        }
}