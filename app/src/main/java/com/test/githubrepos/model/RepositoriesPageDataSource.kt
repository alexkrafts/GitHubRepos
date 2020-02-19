package com.test.githubrepos.model

import androidx.paging.PageKeyedDataSource
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.utils.CoroutineContextProvider
import com.test.githubrepos.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RepositoriesPageDataSource(
    private val repository: GitHubRepository,
    private val scope: CoroutineScope,
    private val contextProvider: CoroutineContextProvider,
    private val query: String
) : PageKeyedDataSource<Int, Repository>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repository>
    ) {
        fetchReposForPage(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val page = params.key
        fetchReposForPage(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val page = params.key
        fetchReposForPage(page, params.requestedLoadSize) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchReposForPage(
        page: Int,
        perPage: Int,
        block: (List<Repository>) -> Unit
    ) {
        scope.launch(contextProvider.IO + handler) {

            delay(200) // throttle user query input

            val response = repository.search(query, page, perPage)
            val results = response.items
            block(results)
        }
    }

    private val handler = CoroutineExceptionHandler { _, e -> e.logError() }
}