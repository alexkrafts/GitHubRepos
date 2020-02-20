package com.test.githubrepos.com.test.githubrepos.model.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Repository(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("forks_count") val forks: Int,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("full_name") val fullName: String,
    val owner: User
)

data class RepositoryDetails(
    val fullName: String,
    val watchersCount: Int
) : Serializable