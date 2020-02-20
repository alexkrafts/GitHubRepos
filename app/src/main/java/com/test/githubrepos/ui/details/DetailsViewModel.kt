package com.test.githubrepos.com.test.githubrepos.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.test.githubrepos.com.test.githubrepos.domain.Failed
import com.test.githubrepos.com.test.githubrepos.domain.Loading
import com.test.githubrepos.com.test.githubrepos.domain.UsersRepository
import com.test.githubrepos.com.test.githubrepos.model.dto.RepositoryDetails
import com.test.githubrepos.com.test.githubrepos.model.dto.User
import com.test.githubrepos.model.WatchersPageDataSource
import com.test.githubrepos.utils.CoroutineContextProvider

class DetailsViewModel(
    repository: RepositoryDetails,
    private val contextProvider: CoroutineContextProvider,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val status = usersRepository.listState

    val name = repository.fullName

    val watchersCount = repository.watchersCount

    val errorMessage = status.map { (it as? Failed)?.error ?: 0 }

    val loading = status.map { it is Loading }

    val pagedList = object : DataSource.Factory<Int, User>() {
        override fun create() =
            WatchersPageDataSource(
                usersRepository = usersRepository,
                scope = viewModelScope,
                contextProvider = contextProvider,
                repositoryName = repository.fullName
            )
    }.let {
        LivePagedListBuilder<Int, User>(it, 10).build()
    }
}