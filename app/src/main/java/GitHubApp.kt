package com.test.githubrepos

import android.app.Application
import com.test.githubrepos.R
import com.test.githubrepos.model.GitHubRepository
import com.test.githubrepos.ui.MainViewModel
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

        single { GitHubRepository(get()) }

        single<CoroutineContextProvider> { CoroutineContextProviderLive() }

        viewModel { MainViewModel(get(), get()) }
    }

}