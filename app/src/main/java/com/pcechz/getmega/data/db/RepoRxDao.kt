package com.pcechz.getmega.data.db

import androidx.paging.PagingSource
import androidx.paging.rxjava2.RxPagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pcechz.getmega.data.api.RepoResponse
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.ItemHolder.Repo
import com.pcechz.getmega.data.model.ItemHolder.RepoRemoteKeys
import io.reactivex.Maybe

@Dao
interface RepoRxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<Repo>)

    @Query("SELECT * FROM repos ORDER BY id ASC")
    fun selectAll(): PagingSource<Int, Repo>

    @Query("SELECT * FROM repos ORDER BY id ASC")
    fun getAll(): List<Repo>

    @Query("DELETE FROM repos WHERE createdAt <= date('now','-2 hours')")
        fun clearRepos2Hours()


    @Query("DELETE FROM repos")
    fun clearRepos()

}

