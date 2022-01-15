package com.pcechz.getmega.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.rxjava2.cachedIn
import com.pcechz.getmega.data.api.RepoResponse
import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.Repo
import com.pcechz.getmega.data.repository.GithubRepository
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.annotation.Resource

class RepoViewModel(private val repository: GithubRepository) : ViewModel() {



        fun getRepo(): Flowable<PagingData<Repo>> {
            return repository
                .getRepos()
                .map { pagingData -> pagingData.filter { true } }
                .cachedIn(viewModelScope)
        }
    }

