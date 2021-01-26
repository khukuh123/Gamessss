package com.miko.gamesss.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.data.GamesRepository
import com.miko.gamesss.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    private lateinit var favoriteViewModel: FavoriteViewModel

    @Mock
    private lateinit var gamesRepository: GamesRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<List<GameList>>

    @Before
    fun setUp() {
        favoriteViewModel = FavoriteViewModel(gamesRepository)
    }

    @Test
    fun `getFavoriteGames's gamesRepository called at least once, return non-null and 10 size result and there's a change on observer`() {
        val dummy = DataDummy.generateGameList().toList()
        val favoriteGames = MutableLiveData<List<GameList>>()
        favoriteGames.value = dummy

        `when`(gamesRepository.getFavoriteGames()).thenReturn(favoriteGames)
        val result = favoriteViewModel.getFavoriteGames().value
        verify(gamesRepository, atLeastOnce()).getFavoriteGames()

        assertNotNull(result)
        assertEquals(10, result?.size)

        favoriteViewModel.getFavoriteGames().observeForever(observer)
        verify(observer).onChanged(dummy)
    }

}