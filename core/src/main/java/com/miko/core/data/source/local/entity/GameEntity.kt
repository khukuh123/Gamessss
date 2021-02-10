package com.miko.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameEntities")
data class GameEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "gameId")
    val id: Int = -1,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "released")
    val released: String = "",
    @ColumnInfo(name = "backgroundImage")
    val backgroundImage: String = "",
    @ColumnInfo(name = "website")
    val website: String = "",
    @ColumnInfo(name = "rating")
    val rating: Double = 0.0,
    @ColumnInfo(name = "metaCritic")
    val metaCritic: Int = 0,
    @ColumnInfo(name = "playtime")
    val playtime: Int = -1,
    @ColumnInfo(name = "platforms")
    val platforms: String = "",
    @ColumnInfo(name = "genres")
    val genres: String = "",
    @ColumnInfo(name = "publishers")
    val publishers: String = "",
    @ColumnInfo(name = "developers")
    val developers: String = "",
    @ColumnInfo(name = "description")
    val description: String = ""
)