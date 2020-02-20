package com.test.githubrepos.com.test.githubrepos.domain

import androidx.lifecycle.MutableLiveData
import com.test.githubrepos.R
import com.test.githubrepos.com.test.githubrepos.model.dto.User
import com.test.githubrepos.model.GitHubApi
import java.io.IOException

class UsersRepository(
    private val api: GitHubApi
) {

    val listState =
        MutableLiveData<PagedListState>(
            EmptyQuery
        )

    suspend fun fetchWatchersWithPagination(
        repoFullName: String,
        page: Int,
        perPage: Int
    ): List<User> {
        listState.postValue(Loading)
        val result = api.getWatchers(
            userName = repoFullName.split('/').first(),
            repoName = repoFullName.split('/').last(),
            page = page,
            perPage = perPage
        )
        listState.postValue(if (result.any()) Data else NoResults)

        return result
    }

    fun reportError(e: Throwable) {
        listState.postValue(
            Failed(
                when (e) {
                    is IOException -> R.string.error_no_internet
                    else -> R.string.generic_error
                }
            )
        )
    }
}