package com.test.githubrepos.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val Main: CoroutineContext
    val IO: CoroutineContext
}

class CoroutineContextProviderLive : CoroutineContextProvider {
    override val Main: CoroutineContext by lazy { Dispatchers.Main }
    override val IO: CoroutineContext by lazy { Dispatchers.IO }
}

class CoroutineContextProviderTest : CoroutineContextProvider {
    override val Main: CoroutineContext by lazy { Dispatchers.Unconfined }
    override val IO: CoroutineContext by lazy { Dispatchers.Unconfined }
}

fun Throwable.logError() {
    Log.e("---Github app log", "Error", this)
}

