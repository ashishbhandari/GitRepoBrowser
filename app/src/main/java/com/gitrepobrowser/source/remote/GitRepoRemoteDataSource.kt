package com.gitrepobrowser.source.remote

import android.content.Context
import android.os.Build
import com.gitrepobrowser.BuildConfig
import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.entities.DataGitRepo
import com.gitrepobrowser.util.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @author ashish
 */
class GitRepoRemoteDataSource
private constructor(private val context: Context) : GitSourceRepoInterface {

    var disposable: Disposable? = null

    init {
        checkNotNull(context)
    }

    private val gitRepoApiService by lazy {
        GitRepoApiService.create()
    }

    override fun loadUserGitRepo(page : Int, perPage : Int, callback: GitSourceRepoInterface.Callback) {

        if(Utils.isNetworkAvailable(context)) {

            disposable = gitRepoApiService.loadGitRepos(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, page, perPage).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe({ result ->
                        if (result.isNotEmpty()) {
                            callback.onRepoLoaded(result)
                        } else {
                            callback.onDataNotAvailable()
                        }
                    }, { error ->
                        error.printStackTrace()
                        callback.onDataRequestFailed()
                    })
        }else{
            callback.onDataRequestFailed()
        }
    }

    override fun saveUserGitRepo(pageId: Int, gitRepos: List<DataGitRepo>) {
        // Not required
    }

    override fun refreshCache() {
        // not required
    }

    override fun deleteAllRepos() {
        disposable?.dispose()
    }


    companion object {

        private var INSTANCE: GitRepoRemoteDataSource? = null

        fun getInstance(context: Context): GitRepoRemoteDataSource {

            if (INSTANCE == null) {
                INSTANCE = GitRepoRemoteDataSource(context)
            }
            return INSTANCE as GitRepoRemoteDataSource
        }
    }

}