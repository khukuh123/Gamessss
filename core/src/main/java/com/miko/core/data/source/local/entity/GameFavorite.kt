package com.miko.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gamesFavorite")
data class GameFavorite(
    @ColumnInfo(name = "gameId")
    @PrimaryKey
    val gameId: Int = -1
)