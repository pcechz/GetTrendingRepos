package com.pcechz.getmega.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.db.RepoDatabase
import com.pcechz.getmega.data.mapper.RepoMapper
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.ItemHolder.Repo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class GithubRxRemoteMediator (
    private val service: repoApi,
    private val database: RepoDatabase,
    private val mapper: RepoMapper,
) : RxRemoteMediator<Int, Repo>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Repo>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)

                        remoteKeys?.nextKey?.minus(1) ?: 1
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.nextKey ?: INVALID_PAGE
                    }
                }
            }
            .flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    service.getData(
                        page = page)
                        .map { mapper.transform(it) }
                        .map { insertToDb(page, loadType, it) }
                        .map<MediatorResult> { MediatorResult.Success(endOfPaginationReached = it.endOfPage) }
                        .onErrorReturn { MediatorResult.Error(it) }
                }

            }.onErrorReturn { MediatorResult.Error(it) }

    }

    @Suppress("DEPRECATION")
    private fun insertToDb(page: Int, loadType: LoadType, data: ItemHolder): ItemHolder {
        database.beginTransaction()

        try {
            if (loadType == LoadType.REFRESH) {
                database.RepoRemoteKeysRxDao().clearRemoteKeys()
                database.RepoRxDao().clearRepos()
                database.RepoRxDao().clearRepos2Hours()
            }

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (data.endOfPage) null else page + 1

            val keys = data.results.map {
                ItemHolder.RepoRemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
            }
//            database.RepoRxDao().clearRepos2Hours()

//            database.RepoRemoteKeysRxDao().insertAll(keys)
//           database.RepoRxDao().insertAll(data.results)
//            database.setTransactionSuccessful()

        } finally {
            database.endTransaction()
        }

        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Repo>): ItemHolder.RepoRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            database.RepoRemoteKeysRxDao().remoteKeysByRepoId(repo.id)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Repo>): ItemHolder.RepoRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { repo ->
            database.RepoRemoteKeysRxDao().remoteKeysByRepoId(repo.id)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Repo>): ItemHolder.RepoRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.RepoRemoteKeysRxDao().remoteKeysByRepoId(id)
            }
        }
    }

    companion object {
        const val INVALID_PAGE = -1
    }
}