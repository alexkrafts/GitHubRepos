package com.test.githubrepos

import com.test.githubrepos.com.test.githubrepos.domain.ReposRepository
import com.test.githubrepos.model.GitHubApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

open class TestBase : KoinTest {

    private lateinit var mockServer: MockWebServer

    protected val appModule = module {
        factory<Interceptor> {
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                println("TEST API: $it")
            }).setLevel(HttpLoggingInterceptor.Level.HEADERS)
        }

        factory {
            OkHttpClient.Builder().addInterceptor(get()).build()
        }

        single {
            Retrofit.Builder()
                .client(get())
                .baseUrl(getMockUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        factory { get<Retrofit>().create(GitHubApi::class.java) }

        factory { ReposRepository(get()) }

    }

    @Before
    fun initialize() {
        configureMockServer()
        startKoin {
           modules(appModule)
        }
    }

    @After
    fun cleanup() {
        stopMockServer()
        stopKoin()
    }

    private fun configureMockServer() {
        mockServer = MockWebServer()
        mockServer.start()
    }

    private fun stopMockServer() {
        mockServer.shutdown()
    }

    private fun getMockUrl() = mockServer.url("/").toString()

    fun mockHttpResponse(fileName: String, responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(fileName))
    )

    private fun getJson(path: String): String {
        val uri = javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}