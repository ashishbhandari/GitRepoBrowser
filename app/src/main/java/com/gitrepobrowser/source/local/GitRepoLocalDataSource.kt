package com.gitrepobrowser.source.local

import android.content.Context
import android.util.Log
import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.dao.GitRepo
import com.gitrepobrowser.source.entities.DataGitRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where


/**
 * @author ashish
 */
class GitRepoLocalDataSource
private constructor(context: Context) : GitSourceRepoInterface {

    init {
        checkNotNull(context)
    }

    override fun loadUserGitRepo(pageId: Int, perPage: Int, callback: GitSourceRepoInterface.Callback) {

        Observable.create<List<DataGitRepo>> { emitter ->
            var realm: Realm? = null
            try {
                realm = Realm.getDefaultInstance()
                val findAll = realm.where<GitRepo>().equalTo("pageId", pageId).findAll()
                val copyFromRealm = realm.copyFromRealm(findAll)
                emitter.onNext(copyFromRealm[0].dataGitRepos)
                emitter.onComplete()
            } finally {
                if (realm != null) {
                    realm.close()
                }
            }

        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({ result ->
            if (result.isNotEmpty()) {
                callback.onRepoLoaded(result)
            } else {
                callback.onDataNotAvailable()
            }
        }, { error ->
            error.printStackTrace()
            callback.onDataNotAvailable()
        })
    }


    override fun saveUserGitRepo(pageId: Int, gitRepos: List<DataGitRepo>) {

        Observable.create<Boolean> { emitter ->

            var realm: Realm? = null

            try {
                realm = Realm.getDefaultInstance()

                val realmGitRepo = RealmList<DataGitRepo>()
                realmGitRepo.addAll(gitRepos)

                val gitRepo = GitRepo(pageId, realmGitRepo)

                realm!!.executeTransaction() { realm ->
                    realm.insertOrUpdate(gitRepo)
                }
                emitter.onNext(true)
                emitter.onComplete()

            } catch (e: Exception) {
                Log.e("GitRepoLocal: ", e.toString())
            } finally {
                if (realm != null) {
                    realm.close()
                }
            }

        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({ result ->

        }, { error ->
            error.printStackTrace()
        })

    }

    override fun refreshCache() {
        // not required
    }

    override fun deleteAllRepos() {
        // realm query to delete all repos from db, if required
    }

    companion object {

        private var INSTANCE: GitRepoLocalDataSource? = null

        fun getInstance(context: Context): GitRepoLocalDataSource {

            if (INSTANCE == null) {
                INSTANCE = GitRepoLocalDataSource(context)
            }
            return INSTANCE as GitRepoLocalDataSource
        }
    }
}