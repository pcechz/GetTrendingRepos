package com.pcechz.getmega.data.api

import com.google.gson.annotations.SerializedName
import com.pcechz.getmega.data.model.ItemHolder.Repo


data class RepoResponse (
    @SerializedName("total_count")
    val total: Int = 0,

    @SerializedName("items")
    val items: List<Repo> = emptyList(),

    val nextPage: Int? = null
)