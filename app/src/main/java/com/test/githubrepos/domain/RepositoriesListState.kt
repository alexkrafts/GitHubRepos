package com.test.githubrepos.com.test.githubrepos.domain

sealed class RepositoriesListState
object Data : RepositoriesListState()
data class Failed(val error: Int?) : RepositoriesListState()
object Loading : RepositoriesListState()
object NoResults : RepositoriesListState()
object EmptyQuery : RepositoriesListState()
