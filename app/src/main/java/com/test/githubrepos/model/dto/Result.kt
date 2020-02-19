package com.test.githubrepos.com.test.githubrepos.model.dto

import com.google.gson.annotations.SerializedName
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository

data class Result(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<Repository>
)