package com.test.githubrepos.com.test.githubrepos.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.test.githubrepos.R
import com.test.githubrepos.com.test.githubrepos.model.dto.RepositoryDetails
import com.test.githubrepos.com.test.githubrepos.ui.details.DetailsActivity
import com.test.githubrepos.databinding.ActivityRepositoriesBinding
import kotlinx.android.synthetic.main.activity_repositories.*
import org.koin.android.viewmodel.ext.android.viewModel

class RepositoriesActivity : AppCompatActivity() {

    private val vm: RepositoriesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRepositoriesBinding>(
            this,
            R.layout.activity_repositories
        )
        binding.vm = vm
        binding.lifecycleOwner = this@RepositoriesActivity

        val adapter =
            RepositoriesAdapter {
                startActivity(Intent(
                    this,
                    DetailsActivity::class.java
                ).apply {
                    putExtra("repository", RepositoryDetails(it.fullName, it.watchersCount))
                })
            }

        reposList.adapter = adapter

        vm.pagedList.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}