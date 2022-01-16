package com.pcechz.getmega.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pcechz.getmega.data.model.ItemHolder.RepoRemoteKeys

@Dao
interface RepoRemoteKeysRxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RepoRemoteKeys>)

    @Query("SELECT * FROM repo_remote_keys WHERE repoId = :repoId")
    fun remoteKeysByRepoId(repoId: Long): RepoRemoteKeys?

    @Query("DELETE FROM repo_remote_keys")
    fun clearRemoteKeys()

}