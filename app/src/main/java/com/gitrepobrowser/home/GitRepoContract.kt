package com.gitrepobrowser.home

import com.gitrepobrowser.BasePresenter
import com.gitrepobrowser.BaseView
import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 *
 * This specifies the contract between the view and the presenter.
 */
interface GitRepoContract {


    interface View : BaseView<Presenter> {

        fun loadGitRepo(dataGitRepos: List<DataGitRepo>)

        fun noDataAvailable()

        fun dataRequestFailed()

    }

    interface Presenter : BasePresenter {

        fun result(requestCode: Int, resultCode: Int)

    }

}