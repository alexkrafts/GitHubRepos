package com.test.githubrepos.model

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.google.gson.annotations.SerializedName
import com.test.githubrepos.BuildConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class GitHubRepository(
    retrofit: Retrofit
) {

    private val api = retrofit.create(GitHubApiInterface::class.java)

    suspend fun search(query: String, page: Int, perPage: Int)
        = api.search(query, page, perPage)

}

interface GitHubApiInterface {

    @GET("search/repositories")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.GithubClientId,
        @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret
    ): Result
}

data class Result(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<RepositoryDto>
)

class RepositoryDto(
    val id: Int,
    val name: String,
    @SerializedName("total_count") val fullName: String
)

class RepositoriesPageDataSource(
    private val dataSource: GitHubRepository,
    //    private val dao: CharacterDtoDao,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, RepositoryDto>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RepositoryDto>
    ) {
        fetchData(1) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepositoryDto>) {
        val page = params.key
        fetchData(page) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepositoryDto>) {
        val page = params.key
        fetchData(page) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchData(
        page: Int,
        block: (List<RepositoryDto>) -> Unit
    ) {
        scope.launch(getJobErrorHandler()) {
            val response = dataSource.search("koin", 0, 10 )
            val results = response.items
            block(results)
        }
    }

    private fun getJobErrorHandler() =
        CoroutineExceptionHandler { _, e ->
            postError(e.message ?: e.toString())
        }

    private fun postError(message: String) {
        Log.e(this::javaClass.name, "An error happened: $message")
    }
}


