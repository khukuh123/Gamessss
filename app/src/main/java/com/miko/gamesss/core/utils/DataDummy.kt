package com.miko.gamesss.core.utils

import com.miko.gamesss.core.data.source.local.entity.GameEntity
import com.miko.gamesss.core.data.source.local.entity.GameFavorite
import com.miko.gamesss.core.domain.model.GameDetail
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.domain.model.Section
import com.miko.gamesss.core.domain.model.SectionItem
import kotlin.random.Random

object DataDummy {

    fun generateGameFavorites(): List<GameFavorite> {
        val mutableList = mutableListOf<GameFavorite>()
        for(i in 0 until 10){
            mutableList.add(
                GameFavorite(Random(i).nextInt())
            )
        }
        return mutableList
    }

    fun generateGameEntity(): GameEntity {
        return GameEntity(
            (0..100).random(),
            "Game 1",
            "[2 February 1995]",
            "https://media.rawg.io/media/games/29c/12.jpg",
            "[www.website.com/games/game-name]",
            5.0,
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
        for(i in 0 until 10){
            mutableList.add(
                GameEntity(
                    (0..100).random(Random(i)),
                    "Game $i",
                "[${(0..28).random(Random(i))} May ${(1980..2020).random(Random(i))}]",
                "https://media.rawg.io/media/games/29c/$i.jpg",
                "[www.web.com/games/game-$i]",
                Random(i).nextDouble(0.0, 5.0),
                (0..70).random(Random(i)),
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
                listOf(SectionItem("${playtime.removeSurrounding("[","]")} Hour"))
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
            "Action, Action, Action",
            "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum ",
            "https://media.rawg.io/media/games/29c/12.jpg",
            listSection
        )
    }

}