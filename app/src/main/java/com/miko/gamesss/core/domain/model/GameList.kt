package com.miko.gamesss.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameList(
    val id: Int = -1,
    val name: String = "",
    val rating: Double = 0.0,
    val image: String = ""
) : Parcelable