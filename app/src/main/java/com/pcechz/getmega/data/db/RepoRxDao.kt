package com.pcechz.getmega.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.Repo
import com.pcechz.getmega.data.model.RepoRemoteKeys

@Dao
interface RepoRxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<Repo>)

    @Query("SELECT * FROM repos ORDER BY id ASC")
    fun selectAll(): PagingSource<Int, Repo>

    @Query("DELETE FROM repos")
    fun clearRepos()

}

