package com.miko.gamesss.core.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.data.source.local.LocalDataSource
import com.miko.gamesss.core.data.source.local.entity.GameEntity
import com.miko.gamesss.core.data.source.local.entity.GameFavorite
import com.miko.gamesss.core.data.source.remote.RemoteDataSource
import com.miko.gamesss.core.utils.AppExecutors
import com.miko.gamesss.core.utils.DataDummy
import com.miko.gamesss.core.utils.LiveDataTestUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class GamesRepositoryTest {

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)
    private val repository = FakeGamesRepository(remote, local, appExecutors)

    private val dummy = DataDummy.generateGameEntities()
    private val gameId = 1240
    private val query = "Portal 2"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `getListGame's local data source called, return non-null and 10 size data`() {
        val gameLists = MutableLiveData<List<GameEntity>>()
        gameLists.value = dummy

        `when`(local.getTopList()).thenReturn(gameLists)
        val result = LiveDataTestUtil.getValue(repository.getListGame())
        verify(local).getTopList()
        assertNotNull(result.data)
        assertEquals(10, result.data?.size)
    }

    @Test
    fun `searchGame's local data source called, return non-null and 10 size data`() {
        val gameLists = MutableLiveData<List<GameEntity>>()
        gameLists.value = dummy

        `when`(local.searchGame(query)).thenReturn(gameLists)
        val result = LiveDataTestUtil.getValue(repository.searchGame(query))
        verify(local).searchGame(query)
        assertNotNull(result.data)
        assertEquals(10, result.data?.size)
    }

    @Test
    fun `getDetailGame's local data source called, return non-null and same value as the given data`() {
        val dummyDetail = DataDummy.generateGameEntity()
        val gameDetail = MutableLiveData<GameEntity>()
        gameDetail.value = dummyDetail

        `when`(local.getGameDetail(gameId.toString())).thenReturn(gameDetail)
        val result = LiveDataTestUtil.getValue(repository.getDetailGame(gameId.toString()))
        verify(local).getGameDetail(gameId.toString())
        assertNotNull(result)
        val actualGameDetail = result.data as GameDetail
        val expectedGameDetail = DataDummy.generateGameDetail()
        assertEquals(expectedGameDetail.name, actualGameDetail.name)
        assertEquals(expectedGameDetail.rating, actualGameDetail.rating, 0.02)
        assertEquals(expectedGameDetail.genres, actualGameDetail.genres)
        assertEquals(expectedGameDetail.description, actualGameDetail.description)
        assertEquals(expectedGameDetail.backgroundImage, actualGameDetail.backgroundImage)
        val actualSectionList = actualGameDetail.listSection
        val expectedSectionList = expectedGameDetail.listSection
        expectedGameDetail.listSection.filter { it.name == "" }
        for (i in expectedSectionList.indices) {
            assertEquals(expectedSectionList[i].name, actualSectionList[i].name)
            for (j in expectedSectionList[i].listItem.indices) {
                assertEquals(expectedSectionList[i].listItem[j], actualSectionList[i].listItem[j])
            }
        }
    }

    @Test
    fun `checkFavoriteStatus's local data source called, return non-null and 1 size data`() {
        val dummyFavorites = DataDummy.generateGameFavorites()
        val gameLists = MutableLiveData<List<GameFavorite>>()
        gameLists.value = dummyFavorites.take(1)

        `when`(local.checkFavoriteStatus(gameId)).thenReturn(gameLists)
        val result = LiveDataTestUtil.getValue(repository.checkFavoriteStatus(gameId))
        verify(local).checkFavoriteStatus(gameId)
        assertNotNull(result)
        assertEquals(1, result.size)
    }

    @Test
    fun `getFavoriteGames's local data source called, return non-null and 10 size data`() {
        val gameLists = MutableLiveData<List<GameEntity>>()
        gameLists.value = dummy

        `when`(local.getFavoriteGames()).thenReturn(gameLists)
        val result = LiveDataTestUtil.getValue(repository.getFavoriteGames())
        verify(local).getFavoriteGames()
        assertNotNull(result)
        assertEquals(10, result.size)
    }
}