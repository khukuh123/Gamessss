package com.miko.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GenresItem(

    @field:SerializedName("name")
    val name: String
)