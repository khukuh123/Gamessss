package com.miko.core.domain.model

data class GameList(
    val id: Int = -1,
    val name: String = "",
    val rating: Double = 0.0,
    val image: String = "",
    val metaCritic: Int = 0
)