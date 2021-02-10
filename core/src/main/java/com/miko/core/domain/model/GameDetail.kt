package com.miko.core.domain.model

data class GameDetail(
    val name: String = "",
    val rating: Double = 0.0,
    val metaCritic: Int = 0,
    val genres: String = "",
    val description: String = "",
    val backgroundImage: String = "",
    val listSection: List<Section> = listOf()
)