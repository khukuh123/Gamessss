package com.miko.gamesss.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.utils.DataDummy
import com.miko.gamesss.vo.Resource
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
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var gamesRepository: GamesRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<List<GameList>>>

    private val query = "Portal 2"

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(gamesRepository)
    }

    @Test
    fun `getSearchResult's gamesRepository called at least once, return a non-null and 10 size result and there's a change on observer`() {
        val dummy = Resource.success(DataDummy.generateGameList().toList())
        val gameLists = MutableLiveData<Resource<List<GameList>>>()
        gameLists.value = dummy

        `when`(gamesRepository.searchGame(query)).thenReturn(gameLists)
        searchViewModel.setSearchQuery(query)
        val result = searchViewModel.getSearchResult().value?.data
        verify(gamesRepository, atLeastOnce()).searchGame(query)

        assertNotNull(result)
        assertEquals(10, result?.size)

        searchViewModel.getSearchResult().observeForever(observer)
        verify(observer).onChanged(dummy)
    }
}