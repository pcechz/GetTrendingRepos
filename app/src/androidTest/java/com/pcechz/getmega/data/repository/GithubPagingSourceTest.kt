package com.pcechz.getmega.data.repository

import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.mapper.RepoMapper
import javax.inject.Inject

 class GithubPagingSourceTest @Inject constructor(
    private val service: repoApi,
    private val mapper: RepoMapper,
)
{


}