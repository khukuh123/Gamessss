package com.miko.gamesss.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.source.GamesRepository
import com.miko.gamesss.utils.DataDummy
import com.miko.gamesss.vo.Resource
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private lateinit var detailViewModel: DetailViewModel

    @Mock
    private lateinit var gamesRepository: GamesRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer1: Observer<Resource<GameDetail>>

    @Mock
    private lateinit var observer2: Observer<List<GameDetail>>


    private val gameId = 1240

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(gamesRepository)
    }

    @Test
    fun `getGameDetail's gamesRepository called at least once, return a non-null and same value with given data and there's a change on observer1`() {
        val dummy = Resource.success(DataDummy.generateGameDetail())
        val gameDetail = MutableLiveData<Resource<GameDetail>>()
        gameDetail.value = dummy

        `when`(gamesRepository.getDetailGame(eq(gameId.toString()))).thenReturn(gameDetail)
        val result = detailViewModel.getGameDetail(gameId.toString()).value
        verify(gamesRepository, atLeastOnce()).getDetailGame(gameId.toString())

        assertNotNull(result)
        val actualGameDetail = result?.data as GameDetail
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

        detailViewModel.getGameDetail(gameId.toString()).observeForever(observer1)
        verify(observer1).onChanged(dummy)
    }

    @Test
    fun `checkGameFavoriteStatus's gamesRepository called at least once, return a non-null and 10 size result and there's a change on observer2`() {
        val dummy = listOf(DataDummy.generateGameDetail())
        val favoriteGames = MutableLiveData<List<GameDetail>>()
        favoriteGames.value = dummy

        `when`(gamesRepository.checkFavoriteStatus(gameId)).thenReturn(favoriteGames)
        val result = detailViewModel.checkGameFavoriteStatus(gameId).value
        verify(gamesRepository, atLeastOnce()).checkFavoriteStatus(gameId)

        assertNotNull(result)
        assertEquals(1, result?.size)

        gamesRepository.checkFavoriteStatus(gameId).observeForever(observer2)
        verify(observer2).onChanged(dummy)
    }
}