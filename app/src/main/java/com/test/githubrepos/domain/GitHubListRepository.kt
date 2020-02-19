package com.test.githubrepos.com.test.githubrepos.domain

import androidx.lifecycle.MutableLiveData
import com.test.githubrepos.R
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.model.GitHubApi
import kotlinx.coroutines.delay
import java.io.IOException

class GitHubListRepository(
    private val api: GitHubApi
) {

    val listState = MutableLiveData<RepositoriesListState>()

    suspend fun searchRepositoriesWithPagination(
        query: String,
        page: Int,
        perPage: Int
    ): List<Repository> {
        val trimQuery = query.trim()
        if (trimQuery.isEmpty()) {
            listState.postValue(EmptyQuery)
            return emptyList()
        }
        listState.postValue(Loading)
        delay(200) // throttle user query input
        val result = api.search(trimQuery, page, perPage)
        listState.postValue(if (result.totalCount == 0) NoResults else Data)
        return result.items
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