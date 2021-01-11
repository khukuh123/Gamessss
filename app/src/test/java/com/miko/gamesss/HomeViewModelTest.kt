package com.miko.gamesss

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.ui.home.HomeViewModel
import com.miko.gamesss.utils.DataDummy
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private val dummy = DataDummy.generateGameList()

    @Mock
    private lateinit var gamesRepository: GamesRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<List<GameList>>

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(gamesRepository)
    }

    @Test
    fun getGameList() {
        val gameList = MutableLiveData<List<GameList>>()
        gameList.value = dummy

        `when`(gamesRepository.getListGame()).thenReturn(gameList)
        val result = homeViewModel.getGameList().value
        verify(gamesRepository).getListGame()
        assertNotNull(result)
        assertEquals(10, result?.size)

        homeViewModel.getGameList().observeForever(observer)
        verify(observer).onChanged(dummy)
    }
}