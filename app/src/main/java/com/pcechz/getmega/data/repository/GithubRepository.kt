package com.pcechz.getmega.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.pcechz.getmega.data.api.repoApi

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val gitHubApi: repoApi) {

    fun getAllRepo() = gitHubApi.getData()

//    fun getAllRepo(query: String) =
//        Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                maxSize = 100,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { GithubPagingSource(gitHubApi, query) }
//        ).liveData
}