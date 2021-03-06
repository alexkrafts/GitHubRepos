package com.test.githubrepos

import android.app.Application
import com.test.githubrepos.com.test.githubrepos.domain.ReposRepository
import com.test.githubrepos.com.test.githubrepos.domain.UsersRepository
import com.test.githubrepos.com.test.githubrepos.model.dto.RepositoryDetails
import com.test.githubrepos.com.test.githubrepos.ui.details.DetailsViewModel
import com.test.githubrepos.com.test.githubrepos.ui.search.RepositoriesViewModel
import com.test.githubrepos.model.GitHubApi
import com.test.githubrepos.utils.CoroutineContextProvider
import com.test.githubrepos.utils.CoroutineContextProviderLive
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GitHubApp)
            modules(appModule)
        }
    }

    private val appModule = module {

        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        factory { get<Retrofit>().create(GitHubApi::class.java) }

        single { ReposRepository(get()) }

        single { UsersRepository(get()) }

        single<CoroutineContextProvider> { CoroutineContextProviderLive() }

        viewModel { RepositoriesViewModel(get(), get()) }
        viewModel { (repo: RepositoryDetails) ->
            DetailsViewModel(repo, get(), get())
        }
    }
}