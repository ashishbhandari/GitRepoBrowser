package com.gitrepobrowser.source

import android.content.Context
import com.gitrepobrowser.source.local.GitRepoLocalDataSource
import com.gitrepobrowser.source.remote.GitRepoRemoteDataSource

/**
 * @author ashish
 */
object GitSourceRepoProvider {

    fun provideGitRepoRepository(context: Context): GitSourceRepo {
        return GitSourceRepo(context, GitRepoRemoteDataSource.getInstance(context), GitRepoLocalDataSource.getInstance(context))
    }
}