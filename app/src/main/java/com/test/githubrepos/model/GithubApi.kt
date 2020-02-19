package com.test.githubrepos.model

import com.test.githubrepos.BuildConfig
import com.test.githubrepos.com.test.githubrepos.model.dto.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/repositories")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.GithubClientId,
        @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret
    ): Result

}


