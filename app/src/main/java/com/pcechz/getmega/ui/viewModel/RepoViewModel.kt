package com.pcechz.getmega.ui.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import com.pcechz.getmega.data.api.RepoResponse
import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.ItemHolder.Repo
import com.pcechz.getmega.data.repository.GithubRepository
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoViewModel(private val repository: GithubRepository) : ViewModel() {



        fun getRepo(): Flowable<PagingData<Repo>> {
            return repository
                .getRepos()
                .map { pagingData -> pagingData.filter { true } }
                .cachedIn(viewModelScope)
        }

    private val repositoryList: Single<MutableList<PagingData<Repo>>>? =
        repository
            .getRepos()
            .map { pagingData -> pagingData.filter { true } }
            .toSortedList{
                    p1, p2 ->
                (p1?.map { it.name }.toString()).compareTo(p2?.map { it.name }.toString())
            }.cache()


    fun getRepositoryLiveData() = repositoryList
    }

