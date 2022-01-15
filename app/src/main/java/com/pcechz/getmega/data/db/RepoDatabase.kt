package com.pcechz.getmega.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pcechz.getmega.data.model.Repo
import com.pcechz.getmega.data.model.RepoRemoteKeys

@Database(
    entities = [Repo::class, RepoRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class RepoDatabase: RoomDatabase() {

    abstract fun RepoRxDao(): RepoRxDao
    abstract fun RepoRemoteKeysRxDao(): RepoRemoteKeysRxDao

    companion object {
        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(
                            context
                        ).also {
                            INSTANCE = it
                        }
                }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                RepoDatabase::class.java, "Repo.db")
                .build()
    }
}
