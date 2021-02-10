package com.miko.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PublishersItem(

    @field:SerializedName("name")
    val name: String
)