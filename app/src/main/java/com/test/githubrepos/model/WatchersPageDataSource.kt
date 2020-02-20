package com.test.githubrepos.model

import androidx.paging.PageKeyedDataSource
import com.test.githubrepos.com.test.githubrepos.domain.UsersRepository
import com.test.githubrepos.com.test.githubrepos.model.dto.User
import com.test.githubrepos.utils.CoroutineContextProvider
import com.test.githubrepos.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WatchersPageDataSource(
    private val usersRepository: UsersRepository,
    private val scope: CoroutineScope,
    private val contextProvider: CoroutineContextProvider,
    private val repositoryName: String
) : PageKeyedDataSource<Int, User>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>
    ) {
        fetchReposWithPagination(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        val page = params.key
        fetchReposWithPagination(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        val page = params.key
        fetchReposWithPagination(page, params.requestedLoadSize) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchReposWithPagination(
        page: Int,
        perPage: Int,
        onLoaded: (List<User>) -> Unit
    ) {
        scope.launch(contextProvider.IO + handler) {
            val list = usersRepository.fetchWatchersWithPagination(repositoryName, page, perPage)
            onLoaded(list)
        }
    }

    private val handler = CoroutineExceptionHandler { _, e ->
        e.logError()
        usersRepository.reportError(e)
    }
}