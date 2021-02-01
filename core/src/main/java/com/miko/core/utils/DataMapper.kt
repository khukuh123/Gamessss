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

    fun fromGameFavoritesToGameDetails(gameFavorite: List<GameFavorite>): List<GameDetail> =
        gameFavorite.map {
            GameDetail()
        }

    fun fromGameIdToFavoriteGame(gameId: Int): GameFavorite = GameFavorite(gameId)

    fun fromGameEntityToGameDetail(gameEntity: GameEntity): GameDetail {
        val listSection = ArrayList<Section>()
        val releasedDate = stringToListOfSectionItem(gameEntity.released)
        listSection.add(
            Section(
                "Released Date",
                releasedDate
            )
        )
        val website = stringToListOfSectionItem(gameEntity.website)
        listSection.add(
            Section(
                "Website",
                website
            )
        )
        val playtime = listOf(SectionItem("${gameEntity.playtime} Hour"))
        listSection.add(
            Section(
                "Average Playtime",
                playtime
            )
        )
        val listPlatforms = stringToListOfSectionItem(gameEntity.platforms)
        listSection.add(
            Section(
                "Platforms",
                listPlatforms
            )
        )
        val listDevelopers = stringToListOfSectionItem(gameEntity.developers)
        listSection.add(
            Section(
                "Developers",
                listDevelopers
            )
        )
        val listPublishers = stringToListOfSectionItem(gameEntity.publishers)
        listSection.add(
            Section(
                "Publishers",
                listPublishers
            )
        )
        return GameDetail(
            gameEntity.name,
            gameEntity.rating,
            gameEntity.genres.removeSurrounding("[", "]"),
            gameEntity.description,
            gameEntity.backgroundImage,
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
                    backgroundImage = i.backgroundImage ?: ""
                )
            )
        }
        return newData
    }

    private fun responseListToString(list: List<Any>): String {
        var result = "["
        for (i in list.indices) {
            var item = list[i]
            lateinit var string: String
            when (item::class) {
                GenresItem::class -> {
                    item = item as GenresItem
                    string = item.name
                }
                PublishersItem::class -> {
                    item = item as PublishersItem
                    string = item.name
                }
                DevelopersItem::class -> {
                    item = item as DevelopersItem
                    string = item.name
                }
                ParentPlatformsItem::class -> {
                    item = item as ParentPlatformsItem
                    string = item.platform.name
                }
                else -> Throwable("Class not found ${item.javaClass}")
            }
            result += if (i != list.lastIndex) {
                "$string, "
            } else {
                "$string]"
            }
        }

        return result
    }

    private fun stringToListOfSectionItem(string: String): List<SectionItem> {
        val result = if (string.isNotEmpty()) string else "-"

        return result.removeSurrounding("[", "]").split(",").map {
            SectionItem(it.trim())
        }.toList()
    }

}