package com.miko.browse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.atLeast
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
class BrowseViewModelTest {
    private lateinit var browseViewModel: BrowseViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var observer: Observer<Resource<out List<GameList>>>

    @Captor
    private lateinit var captor: ArgumentCaptor<Resource<List<GameList>>>

    private lateinit var flow: Flow<Resource<out List<GameList>>>

    companion object {
        const val TYPE = 0
    }

    @Before
    fun setUp() {
        flow = flow {
            emit(Resource.Loading(null))
            kotlinx.coroutines.delay(10)
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesUseCase.getGameListSorted(TYPE)).thenReturn(flow)

        browseViewModel = BrowseViewModel(gamesUseCase)
    }

    @Test
    fun `getGameListSorted's gamesRepository called at least once, return non-null and 10 size result and there's a change on observer`() =
        coroutineScope.runBlockingTest {

            browseViewModel.setGameListSorted(TYPE)
            browseViewModel.getGameListSorted()?.observeForever(observer)
            verify(gamesUseCase).getGameListSorted(TYPE)

            verify(observer).onChanged(captor.capture())
            assertEquals(true, captor.value is Resource.Loading)

            coroutineScope.advanceTimeBy(10)

            verify(observer, atLeast(2)).onChanged(captor.capture())
            assertEquals(true, captor.value is Resource.Success)
            assertNotNull(captor.value.data)
            assertEquals(10, captor.value.data?.size)

        }

}