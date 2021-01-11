package com.miko.gamesss.utils

import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.remote.response.GameListItem
import com.miko.gamesss.model.source.remote.response.GameListResponse
import kotlin.random.Random

object DataDummy {
    fun generateGameList(): ArrayList<GameList>{
        val arrayList = ArrayList<GameList>()
        for(i in 1 until 11){
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

    fun generateGameListResponse(): GameListResponse {
        val dummyGameList = generateGameList()
        val mutableListGame = mutableListOf<GameListItem>()
        for(i in dummyGameList){
            mutableListGame.add(i.toGameListItem())
        }

        return GameListResponse(mutableListGame)
    }

    fun GameList.toGameListItem() = GameListItem(
        this.name,
        this.rating,
        this.id,
        this.image
    )

}