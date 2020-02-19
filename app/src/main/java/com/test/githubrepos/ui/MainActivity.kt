package com.test.githubrepos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        binding.vm = vm
        binding.lifecycleOwner = this@MainActivity

        val adapter = RepositoriesAdapter()
        reposList.adapter = adapter

        vm.pagedList.observe(this, Observer {
            adapter.submitList(it)
        })

    }
}