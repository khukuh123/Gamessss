package com.miko.core.utils

import com.miko.core.data.source.local.entity.GameEntity
import com.miko.core.data.source.local.entity.GameFavorite
import com.miko.core.data.source.remote.response.*
import com.miko.core.domain.model.GameDetail
import com.miko.core.domain.model.GameList
import com.miko.core.domain.model.Section
import com.miko.core.domain.model.SectionItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DataMapper {

    fun fromGameListResponseToGameList(oldData: GameListResponse): List<GameList> {
        val newData = mutableListOf<GameList>()

        oldData.results.forEach {
            newData.add(
                GameList(
                    it.id,
                    it.name,
                    it.rating,
                    it.backgroundImage ?: "",
                    it.metacritic
                )
            )
        }

        return newData
    }

    fun fromGameFavoritesToGameDetails(gameFavorite: List<GameFavorite>): List<GameDetail> =
        gameFavorite.map {
            GameDetail()
        }

    fun fromGameIdToFavoriteGame(gameId: Int): GameFavorite = GameFavorite(gameId)

    fun fromGameEntityToGameDetail(gameEntity: GameEntity?): GameDetail {
        val listSection = ArrayList<Section>()
        val notNullGameEntity = gameEntity ?: GameEntity()
        val releasedDate = stringToListOfSectionItem(notNullGameEntity.released)
        val website = stringToListOfSectionItem(notNullGameEntity.website)
        val playtime = listOf(SectionItem("${notNullGameEntity.playtime} Hour"))
        val listPlatforms = stringToListOfSectionItem(notNullGameEntity.platforms)
        val listDevelopers = stringToListOfSectionItem(notNullGameEntity.developers)
        val listPublishers = stringToListOfSectionItem(notNullGameEntity.publishers)

        listSection.add(
            Section(
                "Released Date",
                releasedDate
            )
        )
        listSection.add(
            Section(
                "Website",
                website
            )
        )
        listSection.add(
            Section(
                "Average Playtime",
                playtime
            )
        )
        listSection.add(
            Section(
                "Platforms",
                listPlatforms
            )
        )
        listSection.add(
            Section(
                "Developers",
                listDevelopers
            )
        )
        listSection.add(
            Section(
                "Publishers",
                listPublishers
            )
        )
        return GameDetail(
            notNullGameEntity.name,
            notNullGameEntity.rating,
            notNullGameEntity.metaCritic,
            notNullGameEntity.genres.removeSurrounding("[", "]"),
            notNullGameEntity.description,
            notNullGameEntity.backgroundImage,
            listSection
        )
    }

    fun fromGameEntitiesToGameLists(gameEntities: List<GameEntity>): List<GameList> =
        gameEntities.map {
            GameList(
                it.id,
                it.name,
                it.rating,
                it.backgroundImage
            )
        }

    fun fromGameDetailResponseToGameEntity(oldData: GameDetailResponse): GameEntity {
        val releasedDate = if (!oldData.released.isNullOrEmpty()) {
            SimpleDateFormat(
                "dd MMMM yyyy",
                Locale.getDefault()
            ).format(
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(oldData.released) as Date
            )
        } else {
            "-"
        }

        return GameEntity(
            oldData.id,
            oldData.name,
            "[${releasedDate}]",
            oldData.backgroundImage ?: "",
            "[${oldData.website}]",
            oldData.rating,
            oldData.metaCritic ?: 0,
            oldData.playtime,
            responseListToString(oldData.parentPlatforms),
            responseListToString(oldData.genres),
            responseListToString(oldData.publishers),
            responseListToString(oldData.developers),
            oldData.descriptionRaw
        )
    }

    fun fromGameListResponseToGameEntities(oldData: GameListResponse): List<GameEntity> {
        val newData = mutableListOf<GameEntity>()

        for (i in oldData.results) {
            newData.add(
                GameEntity(
                    id = i.id,
                    name = i.name,
                    rating = i.rating,
                    metaCritic = i.metacritic,
                    backgroundImage = i.backgroundImage ?: ""
                )
            )
        }
        return newData
    }

    private fun responseListToString(list: List<Any>): String {
        val result = StringBuilder("[")
        if (list.indices.isEmpty()) {
            return ""
        } else {
            for (i in list.indices) {
                val item = list[i]
                lateinit var string: String
                when (item) {
                    is GenresItem -> {
                        string = item.name
                    }
                    is PublishersItem -> {
                        string = item.name
                    }
                    is DevelopersItem -> {
                        string = item.name
                    }
                    is ParentPlatformsItem -> {
                        string = item.platform.name
                    }
                    else -> Throwable("Class not found ${item.javaClass}")
                }
                result.append(
                    if (i != list.lastIndex) {
                        "$string, "
                    } else {
                        "$string]"
                    }
                )
            }
        }

        return result.toString()
    }

    fun stringToListOfSectionItem(string: String): List<SectionItem> {
        val result = if (string.isNotEmpty()) string else "-"

        return result.removeSurrounding("[", "]").split(",").map {
            SectionItem(it.trim())
        }.toList()
    }

}