package com.pcechz.getmega.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pcechz.getmega.data.api.repoApi
import com.pcechz.getmega.data.model.Repo
import com.pcechz.getmega.data.repository.GithubRepository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.annotation.Resource

class RepoViewModel constructor(private val repository: GithubRepository)  : ViewModel() {


    val errorMessage = MutableLiveData<String>()

    var repoList: MutableLiveData<Repo>
    init {
        repoList = MutableLiveData()
    }

    fun getRepoListObserver(): MutableLiveData<Repo> {
        return repoList
    }

    fun makeApiCall(query: String) {

        val retroInstance  = repoApi.getRetroInstance().create(repoApi::class.java)
        retroInstance.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getRepoListObserverRx())
    }

    private fun getRepoListObserverRx(): Observer<Repo> {
        return object :Observer<Repo>{
            override fun onComplete() {
                //hide progress indicator .
            }

            override fun onError(e: Throwable) {
                repoList.postValue(null)
            }

            override fun onNext(t: Repo) {
                repoList.postValue(t)
            }

            override fun onSubscribe(d: Disposable) {
                //start showing progress indicator.
            }
        }
    }
}