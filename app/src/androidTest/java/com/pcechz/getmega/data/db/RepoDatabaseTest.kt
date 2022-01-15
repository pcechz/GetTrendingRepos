package com.pcechz.getmega.data.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pcechz.getmega.data.model.Owner
import com.pcechz.getmega.data.model.Repo
import com.pcechz.getmega.data.model.TestRepoData
import junit.framework.Assert.assertEquals
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

 class RepoDatabaseTest  {

    private lateinit var repoDb: RepoDatabase
    private lateinit var repoDao: RepoRxDao


    @Before
    fun setUpDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repoDb = Room.inMemoryDatabaseBuilder(
            context,
            RepoDatabase::class.java
        ).build()
        repoDao = repoDb.RepoRxDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        repoDb.close()
    }

    @Test
    fun test_inserted_retrieved_repo_match() {
        val repos = listOf(TestRepoData.repos[1], TestRepoData.repos[2])
        repoDao.insertAll(repos)

        val allRepos =  repoDao.getAll()
        assertEquals(repos, allRepos)
    }

    @Test
    fun test_repos_ordered_by_correctly() {
        val repos = listOf(TestRepoData.repos[1],
            TestRepoData.repos[2],
            TestRepoData.repos[3],
            TestRepoData.repos[4])
        repoDao.insertAll(repos)

        val allRepos = repoDao.getAll()
        val expectedRepos = repos.sortedByDescending { it.stars }
        assertEquals(expectedRepos, allRepos)
    }

    @Test
    fun test_conflicting_inserts_replace_repo() {
        val repos = listOf(TestRepoData.repos[1],
            TestRepoData.repos[2],
            TestRepoData.repos[3])

        val repos2 = listOf(
            TestRepoData.repos[1],
            TestRepoData.repos[2],
            TestRepoData.repos[3])
        repoDao.insertAll(repos)
        repoDao.insertAll(repos2)

        val allRepos = repoDao.getAll()
        val expectedRepos = listOf(
            TestRepoData.repos[1],
            TestRepoData.repos[2],
            TestRepoData.repos[3])

        assertEquals(expectedRepos, allRepos)
    }

    @Test
    fun test_limit_repos_per_page_first_page_only_10() {

        val repos = (1..10L).map { Repo(
            it,
            "name $it",
            "fullName $it",
            "Flutter makes it easy and fast to build beautiful apps for mobile and beyond.  $it",
            "https://api.github.com/repos/flutter/flutter  $it",
            200 + it.toInt(),
            200 + it.toInt(),
            "Flutter $it",
            "#4A4A4A $it",
            20 + it.toInt(),
            Owner(
                14101776 + it.toInt(),
                "flutter $it",
                "https://avatars.githubusercontent.com/u/14101776?v=4 $it"


            ),
            "2015-03-06T22:54:58Z $it",
            "2021-04-22T19:01:42Z $it",
            8917 + it.toInt(),
            false

        ) }
        repoDao.insertAll(repos)

        val retrievedRepo = repoDao.getAll()
        assertEquals(10, retrievedRepo.size)
    }


}
