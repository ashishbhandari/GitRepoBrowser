package com.gitrepobrowser.source

import android.content.Context
import com.gitrepobrowser.source.entities.DataGitRepo
import java.util.*

/**
 * @author ashish
 */
class GitSourceRepo
internal constructor(gitRemoteRepoSource: GitSourceRepoInterface, gitLocalRepoSource: GitSourceRepoInterface) : GitSourceRepoInterface {

    private val mGitRepoRemoteDataSource: GitSourceRepoInterface = checkNotNull(gitRemoteRepoSource)

    private val mGitRepoLocalDataSource: GitSourceRepoInterface = checkNotNull(gitLocalRepoSource)


    internal var mCachedGitRepos: MutableMap<Int, List<DataGitRepo>>? = null


    private var mCacheIsDirty = false


    override fun loadUserGitRepo(pageId: Int, perPage: Int, callback: GitSourceRepoInterface.Callback) {
        checkNotNull(callback)

        getRepoFromRemoteDataSource(pageId, perPage, callback)


    }

    private fun getRepoFromRemoteDataSource(pageId: Int, perPage: Int, callback: GitSourceRepoInterface.Callback) {
        mGitRepoRemoteDataSource.loadUserGitRepo(pageId, perPage, object : GitSourceRepoInterface.Callback {
            override fun onRepoLoaded(gitRepos: List<DataGitRepo>) {
                refreshCache(pageId, gitRepos)
                refreshLocalDataSource(pageId, gitRepos)
                callback.onRepoLoaded(ArrayList(mCachedGitRepos?.get(pageId)))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            override fun onDataRequestFailed() {
                callback.onDataRequestFailed()
                getRepoFromLocalDataSource(pageId, perPage, callback)
            }
        })
    }

    private fun getRepoFromLocalDataSource(pageId: Int, perPage: Int, callback: GitSourceRepoInterface.Callback){
        mGitRepoLocalDataSource.loadUserGitRepo(pageId, perPage, object : GitSourceRepoInterface.Callback {

            override fun onRepoLoaded(gitRepos: List<DataGitRepo>) {
                refreshCache(pageId, gitRepos)
                callback.onRepoLoaded(ArrayList(mCachedGitRepos?.get(pageId)))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            override fun onDataRequestFailed() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(pageId: Int, gitRepos: List<DataGitRepo>) {
        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap()
        }

        mCachedGitRepos!!.put(pageId, gitRepos)

        mCacheIsDirty = false
    }

    private fun refreshLocalDataSource(pageId: Int, gitRepos: List<DataGitRepo>) {
        mGitRepoLocalDataSource.deleteAllRepos()
        mGitRepoLocalDataSource.saveUserGitRepo(pageId, gitRepos)
    }

    override fun deleteAllRepos() {
        mGitRepoRemoteDataSource.deleteAllRepos()
        mGitRepoLocalDataSource.deleteAllRepos()

        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap()
        }
        mCachedGitRepos!!.clear()
    }

    override fun saveUserGitRepo(pageId: Int, gitRepos: List<DataGitRepo>) {
        checkNotNull(gitRepos)
        mGitRepoRemoteDataSource.saveUserGitRepo(pageId, gitRepos)
        mGitRepoLocalDataSource.saveUserGitRepo(pageId, gitRepos)
    }

    override fun refreshCache() {
        mCacheIsDirty = true
    }

    companion object {

        private var INSTANCE: GitSourceRepo? = null

        fun getInstance(context: Context?, mGitRepoRemoteDataSource: GitSourceRepoInterface?,
                        mGitRepoLocalDataSource: GitSourceRepoInterface?): GitSourceRepo {
            if (INSTANCE == null) {
                INSTANCE = GitSourceRepo(mGitRepoRemoteDataSource!!, mGitRepoLocalDataSource!!)
            }
            return INSTANCE as GitSourceRepo
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}
