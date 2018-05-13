package com.gitrepobrowser.source.remote

import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.entities.DataGitRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author ashish
 */
class GitRepoRemoteDataSource
private constructor() : GitSourceRepoInterface {

    val gitRepoApiService by lazy {
        GitRepoApiService.create()
    }

    override fun loadUserGitRepo(callback: GitSourceRepoInterface.Callback) {

        gitRepoApiService.loadGitRepos(1, 15).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe({ result ->
                    if (result.isNotEmpty()) {
                        callback.onRepoLoaded(result)
                    } else {
                        callback.onDataNotAvailable()
                    }
                }, { error ->
                    error.printStackTrace()
                })


    }

    override fun saveUserGitRepo(dataGitRepo: DataGitRepo) {
        // Not required
    }

    override fun deleteAllRepos() {
        // Not required
    }


    companion object {

        private var INSTANCE: GitRepoRemoteDataSource? = null

        fun getInstance(): GitRepoRemoteDataSource {

            if (INSTANCE == null) {
                INSTANCE = GitRepoRemoteDataSource()
            }
            return INSTANCE as GitRepoRemoteDataSource
        }
    }

}