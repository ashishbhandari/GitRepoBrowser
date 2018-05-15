package com.gitrepobrowser.source

import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 */
interface GitSourceRepoInterface {


    interface Callback {

        fun onRepoLoaded(tasks: List<DataGitRepo>)

        fun onDataNotAvailable()
    }

    fun loadUserGitRepo(pageId: Int, perPage : Int, callback: Callback)

    fun saveUserGitRepo(dataGitRepo: DataGitRepo)

    fun deleteAllRepos()


}