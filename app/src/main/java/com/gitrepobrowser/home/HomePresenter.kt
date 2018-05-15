package com.gitrepobrowser.home

import android.util.Log
import android.util.MutableInt
import com.gitrepobrowser.source.GitSourceRepo
import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 */
class HomePresenter(gitSourceRepository: GitSourceRepo, tasksView: GitRepoContract.View) : GitRepoContract.Presenter {

    private val mGitRepository: GitSourceRepo

    private val mGitRepoView: GitRepoContract.View

    private var mLastPage : Int = 1


    init {
        mGitRepository = checkNotNull(gitSourceRepository)
        mGitRepoView = checkNotNull(tasksView)

        mGitRepoView.setPresenter(this)
    }

    override fun result(requestCode: Int, resultCode: Int) {

    }

    override fun start() {
        loadTasks(mLastPage)
    }

    override fun loadNextPage() {
        mLastPage += 1
        loadTasks(mLastPage)
    }

    private fun loadTasks(pageId : Int) {
        Log.e("HomePresenter", " ### pageId : "+pageId)
        mGitRepository.loadUserGitRepo(pageId,15,object : GitSourceRepoInterface.Callback {
            override fun onRepoLoaded(tasks: List<DataGitRepo>) {
                Log.e("HomePresenter","Data loaded")
                mGitRepoView.loadGitRepo(tasks)
            }

            override fun onDataNotAvailable() {
                Log.e("HomePresenter","Data not Available!")
                mGitRepoView.noDataAvailable()
            }

        })
    }


}
