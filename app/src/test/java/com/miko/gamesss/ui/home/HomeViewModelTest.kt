package com.miko.gamesss.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.data.GamesRepository
import com.miko.gamesss.core.utils.DataDummy
import com.miko.gamesss.core.data.Resource
import com.nhaarman.mockitokotlin2.atLeastOnce
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    @Mock
    private lateinit var gamesRepository: GamesRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<List<GameList>>>

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(gamesRepository)
    }

    @Test
    fun `getGameList's gamesRepository called at least once, return non-null and 10 size result and there's a change on observer`() {
        val dummy = Resource.success(DataDummy.generateGameList().toList())
        val gameLists = MutableLiveData<Resource<List<GameList>>>()
        gameLists.value = dummy

        `when`(gamesRepository.getListGame()).thenReturn(gameLists)
        homeViewModel.setGameList()
        val result = homeViewModel.getGameList().value?.data
        verify(gamesRepository, atLeastOnce()).getListGame()
        assertNotNull(result)
        assertEquals(10, result?.size)

        homeViewModel.getGameList().observeForever(observer)
        verify(observer).onChanged(dummy)
    }
}