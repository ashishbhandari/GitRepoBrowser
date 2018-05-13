package com.gitrepobrowser.source

import com.gitrepobrowser.source.entities.DataGitRepo
import java.util.*

/**
 * @author ashish
 */
class GitSourceRepo
internal constructor(gitRemoteRepoSource: GitSourceRepoInterface, gitLocalRepoSource: GitSourceRepoInterface) : GitSourceRepoInterface {

    private val mGitRepoRemoteDataSource: GitSourceRepoInterface

    private val mGitRepoLocalDataSource: GitSourceRepoInterface


    internal var mCachedGitRepos: MutableMap<String, DataGitRepo>? = null


    internal var mCacheIsDirty = false


    init {
        mGitRepoRemoteDataSource = checkNotNull(gitRemoteRepoSource)
        mGitRepoLocalDataSource = checkNotNull(gitLocalRepoSource)
    }

    override fun loadUserGitRepo(callback: GitSourceRepoInterface.Callback) {
        checkNotNull(callback)

        // Respond immediately with cache if available and not dirty
        if (mCachedGitRepos != null && !mCacheIsDirty) {
            callback.onRepoLoaded(ArrayList(mCachedGitRepos!!.values))
            return
        }

        if (mCacheIsDirty) {
            // If cache is dirty we need to fetch new data from remote.
            getRepoFromRemoteDataSource(callback)

        } else {
            // Query the local storage if available. If not, query the network.
            mGitRepoLocalDataSource.loadUserGitRepo(object : GitSourceRepoInterface.Callback {
                override fun onRepoLoaded(tasks: List<DataGitRepo>) {
                    refreshCache(tasks)
                    callback.onRepoLoaded(ArrayList(mCachedGitRepos!!.values))
                }

                override fun onDataNotAvailable() {
                    getRepoFromRemoteDataSource(callback)
                }
            })
        }

    }

    private fun refreshCache(gitRepos: List<DataGitRepo>) {
        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap<String, DataGitRepo>()
        }
        mCachedGitRepos!!.clear()
        for (gitRepo in gitRepos) {
            mCachedGitRepos!!.put(gitRepo.id.toString(), gitRepo)
        }
        mCacheIsDirty = false
    }

    private fun getRepoFromRemoteDataSource(callback: GitSourceRepoInterface.Callback) {
        mGitRepoRemoteDataSource.loadUserGitRepo(object : GitSourceRepoInterface.Callback {
            override fun onRepoLoaded(gitRepos: List<DataGitRepo>) {
                refreshCache(gitRepos)
                refreshLocalDataSource(gitRepos)
                callback.onRepoLoaded(ArrayList(mCachedGitRepos!!.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshLocalDataSource(gitRepos: List<DataGitRepo>) {
        mGitRepoLocalDataSource.deleteAllRepos()
        for (gitRepo in gitRepos) {
            mGitRepoLocalDataSource.saveUserGitRepo(gitRepo)
        }
    }

    override fun deleteAllRepos() {
        mGitRepoRemoteDataSource.deleteAllRepos()
        mGitRepoLocalDataSource.deleteAllRepos()

        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap<String, DataGitRepo>()
        }
        mCachedGitRepos!!.clear()
    }

    override fun saveUserGitRepo(dataGitRepo: DataGitRepo) {
        checkNotNull(dataGitRepo)
        mGitRepoRemoteDataSource.saveUserGitRepo(dataGitRepo)
        mGitRepoLocalDataSource.saveUserGitRepo(dataGitRepo)

        // Do in memory cache update to keep the app UI up to date
        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap<String, DataGitRepo>()
        }

        mCachedGitRepos!!.put(dataGitRepo.id.toString(), dataGitRepo)
    }


    companion object {

        private var INSTANCE: GitSourceRepo? = null

        fun getInstance(tasksRemoteDataSource: GitSourceRepoInterface?,
                        tasksLocalDataSource: GitSourceRepoInterface?): GitSourceRepo {
            if (INSTANCE == null) {
                INSTANCE = GitSourceRepo(tasksRemoteDataSource!!, tasksLocalDataSource!!)
            }
            return INSTANCE as GitSourceRepo
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


}
