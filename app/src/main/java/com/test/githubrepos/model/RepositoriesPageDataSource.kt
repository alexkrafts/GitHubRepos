package com.test.githubrepos.model

import androidx.paging.PageKeyedDataSource
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RepositoriesPageDataSource(
    private val dataSource: GitHubRepository,
    //    private val dao: CharacterDtoDao,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Repository>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repository>
    ) {
        fetchData(1) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val page = params.key
        fetchData(page) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val page = params.key
        fetchData(page) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchData(
        page: Int,
        block: (List<Repository>) -> Unit
    ) {
        scope.launch(getJobErrorHandler()) {
            val response = dataSource.search("koin", page, 20)
            val results = response.items
            block(results)
        }
    }

    private fun getJobErrorHandler() =
        CoroutineExceptionHandler { _, e ->
            e.logError()
        }
}