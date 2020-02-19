package com.test.githubrepos.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.model.GitHubRepository
import com.test.githubrepos.model.RepositoriesPageDataSource
import com.test.githubrepos.utils.CoroutineContextProvider
import com.test.githubrepos.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancelChildren

class MainViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val gitHubRepository: GitHubRepository
) : ViewModel() {

    val query = MutableLiveData<String>()

    val status = MutableLiveData<RepositoriesListState>()
        .apply { postValue(RepositoriesListState.Idle) }

    val pagedList = query.switchMap { newQuery ->
        viewModelScope.coroutineContext.cancelChildren()

        val factory = object : DataSource.Factory<Int, Repository>() {
            override fun create() = RepositoriesPageDataSource(
                repository = gitHubRepository,
                scope = viewModelScope,
                contextProvider = contextProvider,
                query = newQuery
            )
        }

        LivePagedListBuilder<Int, Repository>(factory, 10).build()
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { _, t ->
            t.logError()
            status.postValue(
                RepositoriesListState.Error(
                    t
                )
            )
        }
}