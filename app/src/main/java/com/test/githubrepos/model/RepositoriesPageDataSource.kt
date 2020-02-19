package com.test.githubrepos.model

import androidx.paging.PageKeyedDataSource
import com.test.githubrepos.com.test.githubrepos.domain.GitHubListRepository
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.utils.CoroutineContextProvider
import com.test.githubrepos.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RepositoriesPageDataSource(
    private val listRepository: GitHubListRepository,
    private val scope: CoroutineScope,
    private val contextProvider: CoroutineContextProvider,
    private val query: String
) : PageKeyedDataSource<Int, Repository>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repository>
    ) {
        fetchReposWithPagination(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val page = params.key
        fetchReposWithPagination(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val page = params.key
        fetchReposWithPagination(page, params.requestedLoadSize) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchReposWithPagination(
        page: Int,
        perPage: Int,
        onLoaded: (List<Repository>) -> Unit
    ) {
        scope.launch(contextProvider.IO + handler) {
            val list = listRepository.searchRepositoriesWithPagination(query, page, perPage)
            onLoaded(list)
        }
    }

    private val handler = CoroutineExceptionHandler { _, e ->
        e.logError()
        invalidate()
        listRepository.reportError(e)
    }
}