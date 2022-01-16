package com.pcechz.getmega.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.pcechz.getmega.data.db.RepoDatabase
import com.pcechz.getmega.data.model.ItemHolder.Repo
import io.reactivex.Flowable
import javax.inject.Inject

class GithubRxRemoteRepositoryImpl  @Inject constructor  (
    private val database: RepoDatabase,
    private val remoteMediator: GithubRxRemoteMediator
): GithubRepository {

    override fun getRepos(): Flowable<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 100,
                ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.RepoRxDao().selectAll() }
        ).flowable
    }
}
