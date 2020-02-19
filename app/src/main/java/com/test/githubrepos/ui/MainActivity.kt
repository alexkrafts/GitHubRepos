package com.test.githubrepos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.test.githubrepos.R
import com.test.githubrepos.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            vm = vm
            lifecycleOwner = this@MainActivity
        }

        val adapter = RepositoriesAdapter()
        reposList.adapter = adapter
        vm.fetchData()

        vm.getPagedList().observe(this, Observer {
            adapter.submitList(it)
        })

        vm.status.observe(this, Observer {
            when (it) {
                is RepositoriesListState.Error -> {
                    placeholder.text = it.throwable.localizedMessage
                }

                is RepositoriesListState.Data -> {
                    placeholder.isVisible = false
                }
            }
        })
    }
}