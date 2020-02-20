package com.test.githubrepos.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.test.githubrepos.com.test.githubrepos.domain.EmptyQuery
import com.test.githubrepos.com.test.githubrepos.domain.Failed
import com.test.githubrepos.com.test.githubrepos.domain.GitHubListRepository
import com.test.githubrepos.com.test.githubrepos.domain.Loading
import com.test.githubrepos.com.test.githubrepos.domain.NoResults
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.model.RepositoriesPageDataSource
import com.test.githubrepos.utils.CoroutineContextProvider
import kotlinx.coroutines.cancelChildren

class MainViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val gitHubListRepository: GitHubListRepository
) : ViewModel() {

    val query = MutableLiveData<String>()

    private val status = gitHubListRepository.listState

    val loading = status.map { it is Loading }

    val noData = status.map { it is NoResults }

    val enterQuery  = status.map { it is EmptyQuery }

    val errorMessage = status.map { (it as? Failed)?.error ?: 0 }

    val pagedList = query.distinctUntilChanged().switchMap { newQuery ->

        viewModelScope.coroutineContext.cancelChildren()

        val factory = object : DataSource.Factory<Int, Repository>() {
            override fun create() = RepositoriesPageDataSource(
                listRepository = gitHubListRepository,
                scope = viewModelScope,
                contextProvider = contextProvider,
                query = newQuery
            )
        }

        LivePagedListBuilder<Int, Repository>(factory, 10).build()
    }
}