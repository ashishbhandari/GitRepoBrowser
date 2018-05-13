package com.gitrepobrowser.home

import android.util.Log
import com.gitrepobrowser.source.GitSourceRepo
import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 */
class HomePresenter(gitSourceRepository: GitSourceRepo, tasksView: GitRepoContract.View) : GitRepoContract.Presenter {


    private val mGitRepository: GitSourceRepo

    private val mGitRepoView: GitRepoContract.View


    init {
        mGitRepository = checkNotNull(gitSourceRepository)
        mGitRepoView = checkNotNull(tasksView)

        mGitRepoView.setPresenter(this)
    }

    override fun result(requestCode: Int, resultCode: Int) {

    }

    override fun start() {
        loadTasks()
    }

    private fun loadTasks() {

        mGitRepository.loadUserGitRepo(object : GitSourceRepoInterface.Callback {
            override fun onRepoLoaded(tasks: List<DataGitRepo>) {
                Log.e("HomePresenter","Data loaded")
                mGitRepoView.loadGitRepo(tasks)
            }

            override fun onDataNotAvailable() {
                Log.e("HomePresenter","Data not Available!")
            }

        })
    }


}
