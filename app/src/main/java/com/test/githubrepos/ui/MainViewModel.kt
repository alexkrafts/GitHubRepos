package com.test.githubrepos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.test.githubrepos.model.GitHubRepository
import com.test.githubrepos.model.RepositoriesPageDataSource
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.utils.CoroutineContextProvider
import com.test.githubrepos.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val gitHubRepository: GitHubRepository
) : ViewModel() {


    val status = MutableLiveData<RepositoriesListState>()
        .apply { postValue(RepositoriesListState.Idle) }

    fun fetchData() {
        status.postValue(RepositoriesListState.Loading)
        viewModelScope.launch(contextProvider.Main + exceptionHandler) {
            val list = withContext(contextProvider.IO) {
                gitHubRepository.search("koin", 1, 10)
            }


            status.postValue(
                RepositoriesListState.Data(
                    list.items
                )
            )
        }
    }

    fun getPagedList(): LiveData<PagedList<Repository>> {
        val dataSource = RepositoriesPageDataSource(
            gitHubRepository,
            viewModelScope
        )
        val factory = object : DataSource.Factory<Int, Repository>() {
            override fun create() = dataSource
        }
        return LivePagedListBuilder<Int, Repository>(
            factory,
            20
        ).build()
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