package com.gitrepobrowser.source

import android.content.Context
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * @author ashish
 */
class GitSourceRepoTest {

    private var mGitSourceRepo: GitSourceRepo? = null

    @Mock
    private val mGitRepoRemoteDataSource: GitSourceRepo? = null

    @Mock
    private val mGitRepoLocalDataSource: GitSourceRepo? = null

    @Mock
    private val mContext: Context? = null

    @Mock
    private val mGitSourceRepoCallback: GitSourceRepoInterface.Callback? = null


    @Before
    fun setupGitRepository() {
        MockitoAnnotations.initMocks(this)
        // Get a reference to the class under test
        mGitSourceRepo = GitSourceRepo.getInstance(mContext, mGitRepoRemoteDataSource, mGitRepoLocalDataSource)
    }

    @After
    fun destroyRepositoryInstance() {
        GitSourceRepo.destroyInstance()
    }

    @Test
    fun loadUserGitRepo() {

    }

    @Test
    fun deleteAllRepos() {
    }

    @Test
    fun saveUserGitRepo() {
    }

    @Test
    fun refreshCache() {
    }
}