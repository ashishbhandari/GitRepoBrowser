package com.gitrepobrowser.source.local

import android.content.Context
import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 */
class GitRepoLocalDataSource
private constructor(context: Context) : GitSourceRepoInterface {


    init {
        checkNotNull(context)
//        mDbHelper = TasksDbHelper(context)
    }


    override fun loadUserGitRepo(callback: GitSourceRepoInterface.Callback) {
        // realm query to load record


    }

    override fun saveUserGitRepo(dataGitRepo: DataGitRepo) {
        // realm query to save repo into db
    }

    override fun deleteAllRepos() {
        // realm query to delete all repos from db
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