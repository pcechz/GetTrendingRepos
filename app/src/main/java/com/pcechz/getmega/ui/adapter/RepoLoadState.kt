package com.pcechz.getmega.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pcechz.getmega.databinding.RepoLoadStateBinding
import kotlinx.android.synthetic.main.repo_load_state.*

class RepoLoadState(private val retry: () -> Unit) : LoadStateAdapter<RepoLoadState.RepoLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: RepoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RepoLoadStateViewHolder {
        val binding = RepoLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoLoadStateViewHolder(binding)
    }


    inner class RepoLoadStateViewHolder(private val binding: RepoLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener{
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                shimmerContainer.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState !is LoadState.Loading
                noNetImg.isVisible = loadState !is LoadState.Loading
                alientText.isVisible = loadState !is LoadState.Loading
                somethingWrong.isVisible = loadState !is LoadState.Loading
            }
        }

    }





}
