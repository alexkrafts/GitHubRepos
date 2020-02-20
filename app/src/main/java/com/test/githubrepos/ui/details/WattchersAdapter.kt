package com.test.githubrepos.com.test.githubrepos.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.githubrepos.com.test.githubrepos.model.dto.Repository
import com.test.githubrepos.com.test.githubrepos.model.dto.User
import com.test.githubrepos.databinding.ItemRepositoryBinding
import com.test.githubrepos.databinding.ItemWatcherBinding

class WattchersAdapter : PagedListAdapter<User, WattchersAdapter.UserViewHolder>(
    WatcherDiff()
) {

    class WatcherDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            newItem.login == oldItem.login
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWatcherBinding.inflate(inflater, parent, false)
        return UserViewHolder(
            binding.root
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding?.user = getItem(position)
    }

    class UserViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val binding = DataBindingUtil.bind<ItemWatcherBinding>(view)
    }
}