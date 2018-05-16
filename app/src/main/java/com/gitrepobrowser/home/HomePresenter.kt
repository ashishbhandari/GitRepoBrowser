package com.gitrepobrowser.home

import android.util.Log
import com.gitrepobrowser.source.GitSourceRepo
import com.gitrepobrowser.source.GitSourceRepoInterface
import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 *
 * Listens to user actions from the UI, retrieves the data and updates the UI as required.
 *
 */
class HomePresenter(gitSourceRepository: GitSourceRepo, gitRepoView: GitRepoContract.View) : GitRepoContract.Presenter {

    private val mGitRepository: GitSourceRepo = checkNotNull(gitSourceRepository)

    private val mGitRepoView: GitRepoContract.View = checkNotNull(gitRepoView)

    private var mLastPage : Int = 1


    init {
        mGitRepoView.setPresenter(this)
    }

    override fun result(requestCode: Int, resultCode: Int) {

    }

    override fun start() {
        loadUserGitRepos(mLastPage)
    }

    override fun loadNextPage() {
        mLastPage += 1
        loadUserGitRepos(mLastPage)
    }

    private fun loadUserGitRepos(pageId : Int) {
        Log.e("HomePresenter", " ### pageId : "+pageId)
        mGitRepository.loadUserGitRepo(pageId,15,object : GitSourceRepoInterface.Callback {
            override fun onRepoLoaded(gitRepos: List<DataGitRepo>) {
                Log.e("HomePresenter","Data loaded")
                mGitRepoView.loadGitRepo(gitRepos)
            }

            override fun onDataNotAvailable() {
                Log.e("HomePresenter","Data not Available!")
                mGitRepoView.noDataAvailable()
            }

            override fun onDataRequestFailed() {
                mGitRepoView.notifyUser()
            }

        })
    }


}
