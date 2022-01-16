package com.pcechz.getmega.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.pcechz.getmega.data.model.ItemHolder.Repo
import io.reactivex.Flowable

class GitHubRepositoryImpl(private val pagingSource: GithubPagingSource): GithubRepository {

    override fun getRepos(): Flowable<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40),
            pagingSourceFactory = { pagingSource }
        ).flowable
    }
}