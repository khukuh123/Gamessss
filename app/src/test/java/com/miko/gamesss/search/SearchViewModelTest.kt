package com.miko.gamesss.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.core.utils.DataDummy
import com.miko.gamesss.MainCoroutineScopeRule
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mockito.atLeast
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel

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

    companion object {
        const val QUERY = "Portal 2"
    }

    @Before
    fun setUp() {
        flow = flow {
            emit(Resource.Loading(listOf()))
            kotlinx.coroutines.delay(10)
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesUseCase.searchGame(QUERY)).thenReturn(flow)

        searchViewModel = SearchViewModel(gamesUseCase)
    }

    @Test
    fun `getSearchResult's gamesRepository called at least once, return a non-null and 10 size result and there's a change on observer`() =
        runBlocking {
            searchViewModel.getSearchResult(QUERY).observeForever(observer)
            verify(gamesUseCase).searchGame(QUERY)

            verify(observer).onChanged(captor.capture())
            assertEquals(true, captor.value is Resource.Loading)

            coroutineScope.advanceTimeBy(10)

            verify(observer, atLeast(2)).onChanged(captor.capture())
            assertEquals(true, captor.value is Resource.Success)
            assertNotNull(captor.value.data)
            assertEquals(10, captor.value.data?.size)
        }
}