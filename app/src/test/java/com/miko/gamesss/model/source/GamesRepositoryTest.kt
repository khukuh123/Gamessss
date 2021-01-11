package com.miko.gamesss.model.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.miko.gamesss.FakeGamesRepository
import com.miko.gamesss.model.source.remote.RemoteDataSource
import com.miko.gamesss.model.source.remote.response.GameListResponse
import com.miko.gamesss.utils.DataDummy
import com.miko.gamesss.utils.LiveDataTestUtil
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.*

class GamesRepositoryTest {

    private val remote = mock(RemoteDataSource::class.java)
    private val repository = FakeGamesRepository(remote)

    private val gameListResponse = DataDummy.generateGameListResponse()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getListGame() {
        val gameListResponse = MutableLiveData<GameListResponse>()
        gameListResponse.value = DataDummy.generateGameListResponse()

        `when`(remote.getListGame()).thenReturn(gameListResponse)
        val result = LiveDataTestUtil.getValue(repository.getListGame())
        verify(remote).getListGame()
        assertNotNull(result)
        assertEquals(10, result?.size)
    }
}