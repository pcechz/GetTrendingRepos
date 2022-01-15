package com.pcechz.getmega.data.repository


import androidx.paging.PagingData
import com.pcechz.getmega.data.model.Repo
import io.reactivex.Flowable



interface GithubRepository{
    fun getRepos(): Flowable<PagingData<Repo>>
}