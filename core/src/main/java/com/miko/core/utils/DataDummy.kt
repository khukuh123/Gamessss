package com.miko.core.utils

import android.util.Log
import com.miko.core.data.source.local.entity.GameEntity
import com.miko.core.data.source.local.entity.GameFavorite
import com.miko.core.data.source.remote.network.ApiResponse
import com.miko.core.data.source.remote.response.GameDetailResponse
import com.miko.core.data.source.remote.response.GameListItem
import com.miko.core.data.source.remote.response.GameListResponse
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import com.miko.core.domain.model.Section
import com.miko.core.domain.model.SectionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException

object DataDummy {

    fun makeRemoteFlow(response: GameDetailResponse): Flow<ApiResponse<GameDetailResponse>> {
        return flow {
            try {
                emit(ApiResponse.Success(response))
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> emit(ApiResponse.Error("Network Error"))

                    is HttpException -> emit(ApiResponse.Error("${t.code()}\n${t.message()}"))

                    else -> {
                        emit(ApiResponse.Error("Unknown Error\n${t.message.toString()}"))
                        Log.d("RemoteDataSource", t.message.toString())
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun makeRemoteFlow(response: GameListResponse): Flow<ApiResponse<GameListResponse>> {
        return flow {
            try {

                if (response.results.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> emit(ApiResponse.Error("Network Error"))

                    is HttpException -> emit(ApiResponse.Error("${t.code()}\n${t.message()}"))

                    else -> {
                        emit(ApiResponse.Error("Unknown Error\n${t.message.toString()}"))
                        Log.d("RemoteDataSource", t.message.toString())
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun generateGameListResponse(): GameListResponse {
        val listOfGameListItem = mutableListOf<GameListItem>()
        for (i in 0..9) {
            listOfGameListItem.add(
                GameListItem(
                    "Game $i",
                    i.toDouble(),
                    i,
                    79,
                    ""
                )
            )
        }
        return GameListResponse(
            listOfGameListItem
        )
    }

    fun generateGameFavorites(): List<GameFavorite> = listOf(GameFavorite())

    fun generateGameEntity(): GameEntity {
        return GameEntity(
            10,
            "Game 1",
            "[2 February 1995]",
            "https://media.rawg.io/media/games/29c/12.jpg",
            "[www.website.com/games/game-name]",
            5.0,
            78,
            69,
            "[PC, PlayStation, Xbox]",
            "[Action, Action, Action]",
            "[Publisher 1, Publisher 2, Publisher 3]",
            "[Developer 1, Developer 2, Developer 3]",
            "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum ",
        )
    }

    fun generateGameEntities(): List<GameEntity> {
        val mutableList = mutableListOf<GameEntity>()
        for (i in 0 until 10) {
            mutableList.add(
                GameEntity(
                    i,
                    "Game $i",
                    "[$i May 200$i]",
                    "https://media.rawg.io/media/games/29c/$i.jpg",
                    "[www.web.com/games/game-$i]",
                    i.toDouble(),
                    69,
                    78,
                    "[PC, PlayStation, Xbox, Nintendo]",
                    "[Action]",
                    "[Bandai Namco Entertainment, FromSoftware]",
                    "[FromSoftware, QLOC]",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vel lectus ornare, volutpat dolor nec, elementum ligula. Duis id neque varius, sodales libero ac, consequat dolor. Etiam porta ut ligula vel accumsan. Nam nulla libero, semper ut risus nec, gravida venenatis sem. Proin magna arcu, dapibus ut viverra eget, dictum et massa. Mauris accumsan leo sit amet ullamcorper consectetur. Nulla ac venenatis ex. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Phasellus ultrices aliquam felis, ac congue tortor. Aliquam est nibh, varius a sollicitudin et, varius accumsan sem. In nec pellentesque neque, eget faucibus ex."
                )
            )
        }
        return mutableList
    }

    fun generateGameList(): ArrayList<GameList> {
        val arrayList = ArrayList<GameList>()
        for (i in 1 until 11) {
            arrayList.add(
                GameList(
                    i,
                    "Game $i",
                    i.toDouble(),
                    ""
                )
            )
        }

        return arrayList
    }

    fun generateGameDetail(): GameDetail {
        val listSection = ArrayList<Section>()
        val releasedDate = "[2 February 1995]"
        val website = "[www.website.com/games/game-name]"
        val playtime = "[69]"
        val listPlatforms = "[PC, PlayStation, Xbox]"
        val listDevelopers = "[Developer 1, Developer 2, Developer 3]"
        val listPublishers = "[Publisher 1, Publisher 2, Publisher 3]"
        listSection.add(
            Section(
                "Released Date",
                DataMapper.stringToListOfSectionItem(releasedDate)
            )
        )
        listSection.add(
            Section(
                "Website",
                DataMapper.stringToListOfSectionItem(website)
            )
        )
        listSection.add(
            Section(
                "Average Playtime",
                listOf(SectionItem("${playtime.removeSurrounding("[", "]")} Hour"))
            )
        )
        listSection.add(
            Section(
                "Platforms",
                DataMapper.stringToListOfSectionItem(listPlatforms)
            )
        )
        listSection.add(
            Section(
                "Developers",
                DataMapper.stringToListOfSectionItem(listDevelopers)
            )
        )
        listSection.add(
            Section(
                "Publishers",
                DataMapper.stringToListOfSectionItem(listPublishers)
            )
        )

        return GameDetail(
            "Game 1",
            5.0,
            78,
            "Action, Action, Action",
            "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum ",
            "https://media.rawg.io/media/games/29c/12.jpg",
            listSection
        )
    }

}