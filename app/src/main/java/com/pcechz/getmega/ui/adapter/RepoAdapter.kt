package com.pcechz.getmega.ui.adapter

import android.app.LauncherActivity.ListItem
import android.graphics.Color.*
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pcechz.getmega.R
import com.pcechz.getmega.data.model.Repo
import com.pcechz.getmega.databinding.ItemTrendingRepoBinding


class RepoAdapter: PagingDataAdapter<Repo, RepoAdapter.ViewHolder>(REPO_COMPARATOR) {
    private var expandedItem = -1

    companion object {

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
        }
    }


    inner  class ViewHolder(val binding: ItemTrendingRepoBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrendingRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getItem(position).let { repo ->
            with(holder) {
                if (repo != null) {
                    Glide.with(itemView)
                        .load(repo.owner.avatar_url)
                        .centerCrop()
                        .error(android.R.drawable.stat_notify_error)
                        .into(binding.avatar)
                    binding.name.text = repo.owner.login
                    binding.repoName.text = repo.name
                    binding.tvDescription.text = repo.description
                    binding.starCount.text = repo.stars.toString()
                    binding.forkCount.text = repo.forks.toString()
                    repo.language?.let {
                        binding.langName.text = repo.language
                        binding.langColor.visibility = View.VISIBLE
                        binding.langName.visibility = View.VISIBLE
                    } ?: run {
                        binding.langName.visibility = View.GONE
                        binding.langColor.visibility = View.GONE
                    }
                    repo.languageColor?.let {
                        val drawable =
                            binding.root.context.resources.getDrawable(R.drawable.ic_circle) as GradientDrawable
                        drawable.setColor(parseColor(repo.languageColor))
                        binding.langColor.setImageDrawable(drawable)
                    }
                    binding.expandedView.visibility = if (repo.expand) View.VISIBLE else View.GONE
                    binding.cardLayout.setOnClickListener {
                        repo.expand = !repo.expand
                        notifyItemChanged(layoutPosition)
                        if (expandedItem != -1 && expandedItem != layoutPosition) {
                            getItem(expandedItem)?.expand  = false
                                notifyItemChanged(expandedItem)
                        }
                        if (repo.expand) {

                            expandedItem = layoutPosition
                            getItem(expandedItem)?.expand  = true
                            notifyItemChanged(expandedItem)

                        }
                    }

                }

            }
            }



    }




}






