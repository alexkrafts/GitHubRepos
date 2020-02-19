package com.test.githubrepos.com.test.githubrepos.model.dto

import com.google.gson.annotations.SerializedName

data class Owner(
    val id: Int,
    @SerializedName("avatar_url") val avatarUrl: String
)