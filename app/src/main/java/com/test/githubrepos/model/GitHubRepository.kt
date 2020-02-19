package com.test.githubrepos.model

import retrofit2.Retrofit

class GitHubRepository(
    retrofit: Retrofit
) {

    private val api = retrofit.create(GitHubApi::class.java)

    suspend fun search(query: String, page: Int, perPage: Int) = api.search(query, page, perPage)
}