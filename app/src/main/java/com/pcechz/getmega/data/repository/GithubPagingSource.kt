package com.pcechz.getmega.data.repository

import androidx.paging.rxjava2.RxPagingSource
import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.mapper.RepoMapper
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.Repo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.util.*


private const val STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val service: repoApi,
    private val mapper: RepoMapper,
) : RxPagingSource<Int, Repo>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Repo>> {
        val position = params.key ?: 1

        return service.getData(position)
            .subscribeOn(Schedulers.io())
            .map { mapper.transform(it) }
            .map { toLoadResult(it, position) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: ItemHolder, position: Int): LoadResult<Int, Repo> {
        return LoadResult.Page(
            data = data.results,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.itemCount) null else position + 1
        )
    }
}