package com.test.githubrepos.ui

import com.test.githubrepos.com.test.githubrepos.model.dto.Repository

sealed class RepositoriesListState {
    object Idle : RepositoriesListState()
    object Loading : RepositoriesListState()
    data class Error(val throwable: Throwable) : RepositoriesListState()
    data class Data(val list: List<Repository>) : RepositoriesListState()
}