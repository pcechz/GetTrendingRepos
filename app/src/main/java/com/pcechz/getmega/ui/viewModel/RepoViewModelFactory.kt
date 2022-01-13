package com.pcechz.getmega.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pcechz.getmega.data.repository.GithubRepository

class RepoViewModelFactory constructor(private val repository: GithubRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RepoViewModel::class.java)) {
            RepoViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}