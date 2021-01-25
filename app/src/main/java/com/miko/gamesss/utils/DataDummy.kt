package com.miko.gamesss.utils

import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.Section
import kotlin.random.Random

object DataDummy {

    fun generateGameList(): ArrayList<GameList> {
        val arrayList = ArrayList<GameList>()
        for (i in 1 until 11) {
            arrayList.add(
                GameList(
                    Random(i).nextInt(1, 10),
                    "Game $i",
                    Random(i).nextDouble(1.0, 10.0),
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
                RoomEntity.stringToListOfSectionItem(releasedDate)
            )
        )
        listSection.add(
            Section(
                "Website",
                RoomEntity.stringToListOfSectionItem(website)
            )
        )
        listSection.add(
            Section(
                "Average Playtime",
                RoomEntity.stringToListOfSectionItem(playtime)
            )
        )
        listSection.add(
            Section(
                "Platforms",
                RoomEntity.stringToListOfSectionItem(listPlatforms)
            )
        )
        listSection.add(
            Section(
                "Developers",
                RoomEntity.stringToListOfSectionItem(listDevelopers)
            )
        )
        listSection.add(
            Section(
                "Publishers",
                RoomEntity.stringToListOfSectionItem(listPublishers)
            )
        )

        return GameDetail(
            "Game 1",
            5.0,
            "Action, Action, Action",
            "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum ",
            "",
            listSection
        )
    }

}