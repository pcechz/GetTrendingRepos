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
import javax.inject.Inject


private const val STARTING_PAGE_INDEX = 1

class GithubPagingSource  @Inject constructor(
    private val service: repoApi,
    private val mapper: RepoMapper,
) : RxPagingSource<Int, Repo>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Repo>> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return service.getData(position)
            .subscribeOn(Schedulers.io())
            .map { mapper.transform(it) }
            .map { toLoadResult(it, position) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: ItemHolder, position: Int): LoadResult<Int, Repo> {
        return LoadResult.Page(
            data = data.results,
            prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
            nextKey = if (data.results.isEmpty()) null else position + 1
        )


    }
}

