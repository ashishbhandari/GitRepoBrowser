package com.gitrepobrowser.source

import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 */
interface GitSourceRepoInterface {


    interface Callback {

        fun onRepoLoaded(gitRepos: List<DataGitRepo>)

        fun onDataNotAvailable()

        fun onDataRequestFailed()

    }

    fun loadUserGitRepo(pageId: Int, perPage : Int, callback: Callback)

    fun saveUserGitRepo(pageId: Int, gitRepos: List<DataGitRepo>)

    fun deleteAllRepos()

    fun refreshCache()


}