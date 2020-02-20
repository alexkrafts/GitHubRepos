package com.test.githubrepos.com.test.githubrepos.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.databinding.ItemRepositoryBinding

class RepositoriesAdapter(
    private val onItemClick: (Repository) -> Unit
) : PagedListAdapter<Repository, RepositoriesAdapter.RepositoryViewHolder>(
    RepositoryDiff()
) {

    class RepositoryDiff : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            newItem.name == oldItem.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepositoryBinding.inflate(inflater, parent, false)
        return RepositoryViewHolder(
            binding.root,
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.binding?.repo = getItem(position)
    }

    class RepositoryViewHolder(
        view: View,
        onItemClick: (Repository) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        val binding = DataBindingUtil.bind<ItemRepositoryBinding>(view)

        init {
            view.setOnClickListener {
                binding?.repo?.let { onItemClick(it) }
            }
        }
    }
}