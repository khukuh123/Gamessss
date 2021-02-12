package com.miko.gamesss.core.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miko.core.data.Resource
import com.miko.core.data.source.local.LocalDataSource
import com.miko.core.data.source.local.entity.GameEntity
import com.miko.core.data.source.local.entity.GameFavorite
import com.miko.core.data.source.remote.RemoteDataSource
import com.miko.core.data.source.remote.network.ApiResponse
import com.miko.core.data.source.remote.response.GameListResponse
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.repository.IGamesRepository
import com.miko.core.utils.AppExecutors
import com.miko.core.utils.DataDummy
import com.miko.gamesss.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GamesRepositoryTest {

    @Mock
    private lateinit var remote: RemoteDataSource

    @Mock
    private lateinit var local: LocalDataSource

    @Mock
    private lateinit var appExecutors: AppExecutors

    private lateinit var repository: IGamesRepository

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private lateinit var gameEntities: Flow<List<GameEntity>>
    private lateinit var gameEntity: Flow<GameEntity>
    private lateinit var gameFavorites: Flow<List<GameFavorite>>
    private lateinit var gameListResponse: Flow<ApiResponse<GameListResponse>>

    companion object {
        const val GAME_ID = 1240
        const val QUERY = "Portal 2"
        const val DATES = "2020-02-1,2020-02-16"
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository = FakeGamesRepository(remote, local, appExecutors)

        gameEntities = flow {
            emit(DataDummy.generateGameEntities())
        }
        `when`(local.getTopList()).thenReturn(gameEntities)
        `when`(local.searchGame(QUERY)).thenReturn(gameEntities)
        `when`(local.getFavoriteGames()).thenReturn(gameEntities)

        gameEntity = flow {
            emit(DataDummy.generateGameEntity())
        }
        `when`(local.getGameDetail(GAME_ID.toString())).thenReturn(gameEntity)

        gameFavorites = flow {
            emit(DataDummy.generateGameFavorites())
        }
        `when`(local.checkFavoriteStatus(GAME_ID)).thenReturn(gameFavorites)

        gameListResponse = flow {
            emit(ApiResponse.Success(DataDummy.generateGameListResponse()))
        }
    }

    @Test
    fun `getListGame's local data source called, return non-null and 10 size data`(): Unit =
        coroutineScope.runBlockingTest {
            repository.getListGame().collectIndexed { index, value ->
                if (index == 0) {
                    assertEquals(true, value is Resource.Loading)
                } else {

                    assertEquals(true, value is Resource.Success)
                    assertNotNull(value)
                    assertEquals(10, value.data?.size)
                }
            }
            verify(local, atLeast(2)).getTopList()
        }

    @Test
    fun `searchGame's local data source called, return non-null and 10 size data`(): Unit =
        coroutineScope.runBlockingTest {
            repository.searchGame(QUERY).collectIndexed { index, value ->
                if (index == 0) {
                    assertEquals(true, value is Resource.Loading)
                } else {
                    assertEquals(true, value is Resource.Success)
                    assertNotNull(value.data)
                    assertEquals(10, value.data?.size)
                }
            }

            verify(local, atLeast(2)).searchGame(QUERY)
        }

    @Test
    fun `getDetailGame's local data source called, return non-null and same value as the given data`(): Unit =
        coroutineScope.runBlockingTest {
            repository.getDetailGame(GAME_ID.toString()).collectIndexed { index, value ->
                if (index == 0) {
                    assertEquals(true, value is Resource.Loading)
                } else {
                    assertEquals(true, value is Resource.Success)
                    assertNotNull(value)

                    val actualGameDetail = value.data as GameDetail
                    val expectedGameDetail = DataDummy.generateGameDetail()

                    assertEquals(expectedGameDetail.name, actualGameDetail.name)
                    assertEquals(expectedGameDetail.rating, actualGameDetail.rating, 0.02)
                    assertEquals(expectedGameDetail.genres, actualGameDetail.genres)
                    assertEquals(expectedGameDetail.description, actualGameDetail.description)
                    assertEquals(
                        expectedGameDetail.backgroundImage,
                        actualGameDetail.backgroundImage
                    )

                    val actualSectionList = actualGameDetail.listSection
                    val expectedSectionList = expectedGameDetail.listSection

                    expectedGameDetail.listSection.filter { it.name == "" }

                    for (i in expectedSectionList.indices) {
                        assertEquals(expectedSectionList[i].name, actualSectionList[i].name)
                        for (j in expectedSectionList[i].listItem.indices) {
                            assertEquals(
                                expectedSectionList[i].listItem[j],
                                actualSectionList[i].listItem[j]
                            )
                        }
                    }
                }
            }

            verify(local, atLeast(2)).getGameDetail(GAME_ID.toString())
        }

    @Test
    fun `checkFavoriteStatus's local data source called, return non-null and 1 size data`() =
        coroutineScope.runBlockingTest {
            val result = repository.checkFavoriteStatus(GAME_ID).first()
            verify(local).checkFavoriteStatus(GAME_ID)
            assertNotNull(result)
            assertEquals(1, result.size)
        }

    @Test
    fun `getFavoriteGames's local data source called, return non-null and 10 size data`() =
        coroutineScope.runBlockingTest {
            val result = repository.getFavoriteGames().first()
            verify(local).getFavoriteGames()
            assertNotNull(result)
            assertEquals(10, result.size)
        }

    @Test
    fun `getGameListReleased's remote data source called, return non null and 10 size`(): Unit =
        coroutineScope.runBlockingTest {
            `when`(remote.getGameListReleased(DATES)).thenReturn(gameListResponse)

            repository.getGameListReleased(DATES).collectIndexed { index, value ->
                if (index == 0) {
                    assertEquals(true, value is Resource.Loading)
                } else {
                    assertEquals(true, value is Resource.Success)

                    assertNotNull(value)
                    assertEquals(10, value.data?.size)
                }
            }

            verify(remote).getGameListReleased(DATES)
        }

    @Test
    fun `getGameListMetaCritic's remote data source called, return non null and 10 size`(): Unit =
        coroutineScope.runBlockingTest {
            `when`(remote.getGameListMetaCritic()).thenReturn(gameListResponse)

            repository.getGameListMetaCritic().collectIndexed { index, value ->
                if (index == 0) {
                    assertEquals(true, value is Resource.Loading)
                } else {
                    assertEquals(true, value is Resource.Success)

                    assertNotNull(value)
                    assertEquals(10, value.data?.size)
                }
            }

            verify(remote).getGameListMetaCritic()
        }

}