package com.pcechz.getmega.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.pcechz.getmega.R
import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.db.RepoDatabase
import com.pcechz.getmega.data.mapper.RepoMapper
import com.pcechz.getmega.data.repository.GitHubRepositoryImpl
import com.pcechz.getmega.data.repository.GithubPagingSource
import com.pcechz.getmega.data.repository.GithubRxRemoteMediator
import com.pcechz.getmega.data.repository.GithubRxRemoteRepositoryImpl
import com.pcechz.getmega.ui.viewModel.RepoViewModelFactory
import java.util.*

object MyInjection {
    private fun provideDatabase(context: Context): RepoDatabase = RepoDatabase.getInstance(context)


    fun provideRxViewModel(context: Context): ViewModelProvider.Factory {
        val pagingSource =
            GithubPagingSource(
                service = repoApi.create(),
                mapper = RepoMapper()
            )

        val repository =
            GitHubRepositoryImpl(
                pagingSource = pagingSource
            )

        return RepoViewModelFactory(
            repository
        )
    }

    fun provideRxRemoteViewModel(context: Context): ViewModelProvider.Factory {
        val remoteMediator =
            GithubRxRemoteMediator(
                service = repoApi.create(),
                database = provideDatabase(context),
                mapper = RepoMapper()
            )

        val repository =
            GithubRxRemoteRepositoryImpl(
                database = provideDatabase(context),
                remoteMediator = remoteMediator
            )

        return RepoViewModelFactory(
            repository
        )
    }
}