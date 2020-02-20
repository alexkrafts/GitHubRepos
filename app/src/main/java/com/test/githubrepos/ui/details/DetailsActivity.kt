package com.test.githubrepos.com.test.githubrepos.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.test.githubrepos.R
import com.test.githubrepos.com.test.githubrepos.model.dto.RepositoryDetails
import com.test.githubrepos.databinding.ActivityDetailsBinding
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsActivity : AppCompatActivity() {

    private lateinit var repoName: RepositoryDetails

    private val vm: DetailsViewModel by viewModel { parametersOf(repoName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repoName = intent.getSerializableExtra("repository") as RepositoryDetails

        val binding = DataBindingUtil.setContentView<ActivityDetailsBinding>(
            this,
            R.layout.activity_details
        )
        binding.vm = vm
        binding.lifecycleOwner = this@DetailsActivity

        val adapter = WattchersAdapter()
        watchersList.adapter = adapter

        vm.pagedList.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}