package com.test.githubrepos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.test.githubrepos.R
import com.test.githubrepos.model.RepositoriesPageDataSource
import com.test.githubrepos.model.GitHubRepository
import com.test.githubrepos.model.RepositoryDto
import com.test.githubrepos.utils.CoroutineContextProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class MainViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val gitHubRepository: GitHubRepository
) : ViewModel() {

    init{
        fetchData()
    }

    val status = MutableLiveData<RepositoriesListState>()
        .apply { postValue(RepositoriesListState.Idle) }

    fun fetchData() {
        status.postValue(RepositoriesListState.Loading)
        viewModelScope.launch(handler) {
            val list =
                withContext(contextProvider.IO) { gitHubRepository.search("koin",1,10) }


            status.setValue(RepositoriesListState.Data(list.items))
        }
    }

    fun getPagedList(): LiveData<PagedList<RepositoryDto>> {
        val dataSource = RepositoriesPageDataSource(gitHubRepository, viewModelScope)
        val factory = object : DataSource.Factory<Int, RepositoryDto>() {
            override fun create() = dataSource
        }
        return LivePagedListBuilder<Int, RepositoryDto>(factory, 20).build()
    }

    private val handler = contextProvider.Main + CoroutineExceptionHandler { _, t ->
        status.postValue(RepositoriesListState.Error(t))
    }
}


sealed class RepositoriesListState {
    object Idle : RepositoriesListState()
    object Loading : RepositoriesListState()
    data class Error(val throwable: Throwable) : RepositoriesListState()
    data class Data(val list: List<RepositoryDto>) : RepositoriesListState()
}

