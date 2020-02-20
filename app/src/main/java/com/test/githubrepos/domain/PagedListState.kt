package com.test.githubrepos.com.test.githubrepos.domain

sealed class PagedListState
object Data : PagedListState()
data class Failed(val error: Int?) : PagedListState()
object Loading : PagedListState()
object NoResults : PagedListState()
object EmptyQuery : PagedListState()
