package com.miko.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Platform(

    @field:SerializedName("name")
    val name: String
) : Parcelable