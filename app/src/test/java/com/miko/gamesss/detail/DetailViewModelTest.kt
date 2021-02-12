package com.miko.gamesss.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.core.utils.DataDummy
import com.miko.gamesss.MainCoroutineScopeRule
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
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private lateinit var detailViewModel: DetailViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var observer1: Observer<Resource<GameDetail>>

    @Mock
    private lateinit var observer2: Observer<List<GameDetail>>

    @Captor
    private lateinit var captor: ArgumentCaptor<Resource<GameDetail>>

    @Captor
    private lateinit var captor1: ArgumentCaptor<List<GameDetail>>

    private lateinit var flow: Flow<Resource<GameDetail>>
    private lateinit var flow1: Flow<List<GameDetail>>

    companion object {
        const val GAME_ID = 1240
    }

    @Before
    fun setUp() {
        flow = flow {
            emit(Resource.Loading(GameDetail()))
            kotlinx.coroutines.delay(10)
            emit(Resource.Success(DataDummy.generateGameDetail()))
        }
        `when`(gamesUseCase.getDetailGame(GAME_ID.toString())).thenReturn(flow)


        flow1 = flow {
            emit(listOf(DataDummy.generateGameDetail()))
        }
        `when`(gamesUseCase.checkFavoriteStatus(GAME_ID)).thenReturn(flow1)

        detailViewModel = DetailViewModel(gamesUseCase)
    }

    @Test
    fun `getGameDetail's gamesRepository called at least once, return a non-null and same value with given data and there's a change on observer1`() =
        coroutineScope.runBlockingTest {

            detailViewModel.getGameDetail(GAME_ID.toString()).observeForever(observer1)
            verify(gamesUseCase, atLeastOnce()).getDetailGame(GAME_ID.toString())

            verify(observer1).onChanged(captor.capture())
            assertEquals(true, captor.value is Resource.Loading)

            coroutineScope.advanceTimeBy(10)

            verify(observer1, atLeast(2)).onChanged(captor.capture())
            assertEquals(true, captor.value is Resource.Success)
            assertNotNull(captor.value.data)
            val actualGameDetail = captor.value?.data as GameDetail
            val expectedGameDetail = DataDummy.generateGameDetail()
            assertEquals(expectedGameDetail.name, actualGameDetail.name)
            assertEquals(expectedGameDetail.rating, actualGameDetail.rating, 0.02)
            assertEquals(expectedGameDetail.genres, actualGameDetail.genres)
            assertEquals(expectedGameDetail.description, actualGameDetail.description)
            assertEquals(expectedGameDetail.backgroundImage, actualGameDetail.backgroundImage)
            val actualSectionList = actualGameDetail.listSection
            val expectedSectionList = expectedGameDetail.listSection
            for (i in expectedSectionList.indices) {
                assertEquals(expectedSectionList[i].name, actualSectionList[i].name)
                for (j in expectedSectionList[i].listItem.indices) {
                    assertEquals(
                        expectedSectionList[i].listItem[j].name,
                        actualSectionList[i].listItem[j].name
                    )
                }
            }
        }

    @Test
    fun `checkGameFavoriteStatus's gamesRepository called at least once, return a non-null and 10 size result and there's a change on observer2`() =
        coroutineScope.runBlockingTest {

            detailViewModel.checkGameFavoriteStatus(GAME_ID).observeForever(observer2)
            verify(gamesUseCase, atLeastOnce()).checkFavoriteStatus(GAME_ID)

            verify(observer2).onChanged(captor1.capture())
            assertNotNull(captor1.value)
            assertEquals(1, captor1.value?.size)
        }
}