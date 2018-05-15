package com.gitrepobrowser.source

import android.content.Context
import com.gitrepobrowser.source.entities.DataGitRepo
import com.gitrepobrowser.util.Utils
import java.util.*

/**
 * @author ashish
 */
class GitSourceRepo
internal constructor(private val context: Context, gitRemoteRepoSource: GitSourceRepoInterface, gitLocalRepoSource: GitSourceRepoInterface) : GitSourceRepoInterface {

    private val mGitRepoRemoteDataSource: GitSourceRepoInterface

    private val mGitRepoLocalDataSource: GitSourceRepoInterface


    internal var mCachedGitRepos: MutableMap<Int, List<DataGitRepo>>? = null


    internal var mCacheIsDirty = false


    init {
        mGitRepoRemoteDataSource = checkNotNull(gitRemoteRepoSource)
        mGitRepoLocalDataSource = checkNotNull(gitLocalRepoSource)
    }

    override fun loadUserGitRepo(pageId: Int, perPage: Int, callback: GitSourceRepoInterface.Callback) {
        checkNotNull(callback)

        if (Utils.isNetworkAvailable(context)) {
            // If cache is dirty we need to fetch new data from remote.
            getRepoFromRemoteDataSource(pageId, perPage, callback)

        } else {
            // Query the local storage if available. If not, query the network.
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

    }

    private fun refreshCache(pageId: Int, gitRepos: List<DataGitRepo>) {
        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap<Int, List<DataGitRepo>>()
        }
//        mCachedGitRepos!!.clear()

        mCachedGitRepos!!.put(pageId, gitRepos)

        mCacheIsDirty = false
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
            }
        })
    }

    private fun refreshLocalDataSource(pageId: Int, gitRepos: List<DataGitRepo>) {
        mGitRepoLocalDataSource.deleteAllRepos()
        mGitRepoLocalDataSource.saveUserGitRepo(pageId, gitRepos)
    }

    override fun deleteAllRepos() {
        mGitRepoRemoteDataSource.deleteAllRepos()
        mGitRepoLocalDataSource.deleteAllRepos()

        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap<Int, List<DataGitRepo>>()
        }
        mCachedGitRepos!!.clear()
    }

    override fun saveUserGitRepo(pageId: Int, gitRepos: List<DataGitRepo>) {
        checkNotNull(gitRepos)
        mGitRepoRemoteDataSource.saveUserGitRepo(pageId, gitRepos)
        mGitRepoLocalDataSource.saveUserGitRepo(pageId, gitRepos)

        // Do in memory cache update to keep the app UI up to date
        if (mCachedGitRepos == null) {
            mCachedGitRepos = LinkedHashMap<Int, List<DataGitRepo>>()
        }

//        mCachedGitRepos!!.put(dataGitRepo.id.toString(), dataGitRepo)
    }

    override fun refreshCache() {
        mCacheIsDirty = true
    }

}
