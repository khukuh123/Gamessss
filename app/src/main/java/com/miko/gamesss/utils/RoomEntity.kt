package com.miko.gamesss.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.miko.gamesss.model.GameList
import com.miko.gamesss.model.source.local.entity.GameEntity
import com.miko.gamesss.model.source.remote.response.GameListResponse

object RoomEntity {
    fun toGameLists(gameEntities: LiveData<List<GameEntity>>): LiveData<List<GameList>> {
        return Transformations.map(gameEntities){ oldData ->
            val newData = mutableListOf<GameList>()
            for(i in oldData){
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
    }

    fun fromGameListResponse(oldData: GameListResponse): List<GameEntity>{
        val newData = mutableListOf<GameEntity>()
        for (i in oldData.results){
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
}