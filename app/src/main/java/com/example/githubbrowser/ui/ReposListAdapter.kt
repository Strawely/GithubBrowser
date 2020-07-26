package com.example.githubbrowser.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubbrowser.R
import com.example.githubbrowser.RepoNavigateIntent
import com.example.githubbrowser.model.RepositoryDto
import com.example.githubbrowser.databinding.ItemRepositoryBinding

class ReposListAdapter(private val onItemClick: (RepoNavigateIntent) -> Unit) :
    PagedListAdapter<RepositoryDto, ReposListAdapter.RepositoryViewHolder>(
        diffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RepositoryViewHolder(view: View, private val onItemClick: (RepoNavigateIntent) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemRepositoryBinding.bind(view)
        fun bind(repository: RepositoryDto?) {
            if (repository == null) return
            with(repository) {
                binding.textRepoName.text = name
                binding.textOwner.text = owner.login
                binding.root.setOnClickListener {
                    onItemClick(RepoNavigateIntent(owner.avatarUrl, owner.login, name, commitsUrl))
                }
                Glide.with(itemView)
                    .load(repository.owner.avatarUrl)
                    .into(binding.imgAvatar)
            }
        }
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<RepositoryDto>() {
    override fun areItemsTheSame(oldItem: RepositoryDto, newItem: RepositoryDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RepositoryDto, newItem: RepositoryDto): Boolean {
        return oldItem == newItem
    }

}