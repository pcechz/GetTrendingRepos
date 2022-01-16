package com.pcechz.getmega.data.api

import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.ItemHolder.Repo
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface repoApi {


    @GET("search/repositories?q=?sort=stars")
    fun getData(
        @Query("page") page: Int,
    ) : Single<RepoResponse>

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        fun  create() : repoApi {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(repoApi::class.java)
        }
    }


}