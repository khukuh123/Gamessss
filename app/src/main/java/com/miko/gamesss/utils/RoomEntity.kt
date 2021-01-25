package com.miko.gamesss.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.Section
import com.miko.gamesss.model.SectionItem
import com.miko.gamesss.model.source.local.entity.GameEntity
import com.miko.gamesss.model.source.local.entity.GameFavorite
import com.miko.gamesss.model.source.remote.response.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object RoomEntity {

    fun checkFavoriteStatus(gameFavorite: LiveData<List<GameFavorite>>): LiveData<List<GameDetail>> =
        Transformations.map(gameFavorite) { oldData ->
            val newData = mutableListOf<GameDetail>()
            for (i in oldData) {
                newData.add(
                    GameDetail()
                )
            }
            newData
        }

    fun fromGameIdToFavoriteGame(gameId: Int): GameFavorite = GameFavorite(gameId)

    fun fromGameEntityToGameDetail(gameEntity: LiveData<GameEntity>): LiveData<GameDetail> =
        Transformations.map(gameEntity) { oldData ->
            val listSection = ArrayList<Section>()
            val releasedDate = stringToListOfSectionItem(oldData.released)
            listSection.add(
                Section(
                    "Released Date",
                    releasedDate
                )
            )
            val website = stringToListOfSectionItem(oldData.website)
            listSection.add(
                Section(
                    "Website",
                    website
                )
            )
            val playtime = listOf(SectionItem("${oldData.playtime} Hour"))
            listSection.add(
                Section(
                    "Average Playtime",
                    playtime
                )
            )
            val listPlatforms = stringToListOfSectionItem(oldData.platforms)
            listSection.add(
                Section(
                    "Platforms",
                    listPlatforms
                )
            )
            val listDevelopers = stringToListOfSectionItem(oldData.developers)
            listSection.add(
                Section(
                    "Developers",
                    listDevelopers
                )
            )
            val listPublishers = stringToListOfSectionItem(oldData.publishers)
            listSection.add(
                Section(
                    "Publishers",
                    listPublishers
                )
            )
            GameDetail(
                oldData.name,
                oldData.rating,
                oldData.genres.removeSurrounding("[", "]"),
                oldData.description,
                oldData.backgroundImage,
                listSection
            )
        }

    fun fromGameEntitiesToGameLists(gameEntities: LiveData<List<GameEntity>>): LiveData<List<GameList>> =
        Transformations.map(gameEntities) { oldData ->
            val newData = mutableListOf<GameList>()
            for (i in oldData) {
                newData.add(
                    GameList(
                        i.id,
                        i.name,
                        i.rating,
                        i.backgroundImage
                    )
                )
            }
            newData
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

    fun stringToListOfSectionItem(string: String): List<SectionItem> {
        val result = if (string.isNotEmpty()) string else "-"

        return result.removeSurrounding("[", "]").split(",").map {
            SectionItem(it.trim())
        }.toList()
    }

}