package com.test.githubrepos.com.test.githubrepos.model.dto

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("forks_count") val forks: Int,
    @SerializedName("full_name") val fullName: String,
    val owner: Owner
)