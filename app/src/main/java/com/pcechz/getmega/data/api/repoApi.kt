package com.pcechz.getmega.data.api

import com.pcechz.getmega.data.model.Repo
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface repoApi {

    @GET("search/repositories?sort=stars")
    fun getData() : Observable<Repo>

    companion object {
        const val BASE_URL = "https://api.github.com/"
        fun getRetroInstance() : Retrofit {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }


}