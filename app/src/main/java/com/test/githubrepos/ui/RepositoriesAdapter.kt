package com.test.githubrepos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.githubrepos.R
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import kotlinx.android.synthetic.main.item_repository.view.*

class RepositoriesAdapter
    : PagedListAdapter<Repository, RepositoriesAdapter.RepositoryViewHolder>(RepositoryDiff()) {

    class RepositoryDiff : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            newItem.name == oldItem.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) } ?: holder.clear()
    }

    class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // TODO: use databinding
        private val name = view.name
        private val forks = view.forks
        private val description = view.description
        private val avatar = view.avatar

        fun bind(Repository: Repository) {
            name.text = Repository.name
            forks.text = "Forks: ${Repository.forks}"
            description.text = Repository.description
            Picasso.get()
                .load(Repository.owner.avatarUrl)
                .into(avatar)
        }

        fun clear() {
            name.text = ""
            forks.text = ""
            description.text = ""
        }
    }
}