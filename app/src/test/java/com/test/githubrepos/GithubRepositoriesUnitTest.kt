package com.test.githubrepos

import com.test.githubrepos.com.test.githubrepos.domain.EmptyQuery
import com.test.githubrepos.com.test.githubrepos.domain.ReposRepository
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import org.koin.test.inject
import retrofit2.HttpException
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class GithubRepositoriesUnitTest : TestBase() {

    private val repo by inject<ReposRepository>()

    @Test
    fun `checking koin modules`() {
        koinApplication { modules(appModule) }.checkModules()
    }

    @Test
    fun `search tetris repository by name and succeed`() {
        mockHttpResponse("mock_repository_response.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val list = repo.searchRepositoriesWithPagination("tetris", 1, 1)
            assertEquals(1, list.size)
            val tetris = list.first()
            assertEquals(3081286, tetris.id)
            assertEquals("Tetris", tetris.name)
            assertEquals("dtrupenn/Tetris", tetris.fullName)
            assertEquals("A C implementation of Tetris", tetris.description)
            assertEquals(42, tetris.forks)
            assertEquals(872147, tetris.owner.id)
            assertEquals("some beautiful tetris avatar", tetris.owner.avatarUrl)
        }
    }

    @Test(expected = HttpException::class)
    fun `search repository by name and fail`() {
        mockHttpResponse("mock_repository_response.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            repo.searchRepositoriesWithPagination("wtvr", -1, -1)
        }
    }

    @Test
    fun `search empty name repository return empty list despite an exception`() {
        mockHttpResponse("mock_repository_response.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            val list = repo.searchRepositoriesWithPagination("", -1, -1)
            assertEquals(emptyList<Repository>(), list)
            assertEquals(EmptyQuery, repo.listState.value)
        }
    }
}