package com.gitrepobrowser.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gitrepobrowser.R
import com.gitrepobrowser.source.entities.DataGitRepo
import kotlinx.android.synthetic.main.home_frag.*

/**
 * @author ashish
 */
class HomeFragment : Fragment(), GitRepoContract.View {

    private var mPresenter: GitRepoContract.Presenter? = null

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var mGitRepos: MutableList<DataGitRepo>? = null

    private var isLastPageReached: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.home_frag, container, false)
        val githubrepo_rv = root?.findViewById<RecyclerView>(R.id.githubrepo_rv)

        linearLayoutManager = LinearLayoutManager(activity)
        githubrepo_rv?.layoutManager = linearLayoutManager

//        githubrepo_rv?.layoutManager = LinearLayoutManager(activity)
        githubrepo_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val totalItemCount = recyclerView!!.layoutManager.itemCount
                if (totalItemCount == linearLayoutManager.findLastVisibleItemPosition() + 1 && !isLastPageReached) {
                    Log.e("HomeFragment", " #### Inside last Item visible ")
                    mPresenter!!.loadNextPage()
                } else {
                    Log.e("HomeFragment", " #### scroll")
                }
            }
        })

        return root
    }


    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun loadGitRepo(gitRepos: List<DataGitRepo>) {
        if (mGitRepos == null) {
            mGitRepos = ArrayList<DataGitRepo>()
            mGitRepos?.addAll(gitRepos)
            githubrepo_rv.adapter = GitRepoAdapter(mGitRepos as ArrayList<DataGitRepo>)
            (githubrepo_rv.adapter as GitRepoAdapter).addLoadingProgress()
        } else {
            (githubrepo_rv.adapter as GitRepoAdapter).removeLoadingProgress()
            mGitRepos?.addAll(mGitRepos?.size!!, gitRepos)
            (githubrepo_rv.adapter as GitRepoAdapter).notifyDataSetChanged()
            (githubrepo_rv.adapter as GitRepoAdapter).addLoadingProgress()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.start()
    }

    override fun setPresenter(presenter: GitRepoContract.Presenter) {
        mPresenter = checkNotNull(presenter)
    }

    override fun noDataAvailable() {
        isLastPageReached = true
        (githubrepo_rv.adapter as GitRepoAdapter).removeLoadingProgress()
        (githubrepo_rv.adapter as GitRepoAdapter).notifyDataSetChanged()
    }

    override fun dataRequestFailed() {
        Toast.makeText(activity,"Data Request has been failed!!",Toast.LENGTH_LONG).show()
        mPresenter!!.loadNextPage()
    }


}