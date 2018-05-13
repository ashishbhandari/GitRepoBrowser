package com.gitrepobrowser.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gitrepobrowser.R
import com.gitrepobrowser.source.entities.DataGitRepo
import kotlinx.android.synthetic.main.home_frag.*

/**
 * @author ashish
 */
class HomeFragment : Fragment(), GitRepoContract.View {

    private var mPresenter: GitRepoContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.home_frag, container, false)
        val githubrepo_rv = root?.findViewById<RecyclerView>(R.id.githubrepo_rv)
        githubrepo_rv?.layoutManager = LinearLayoutManager(activity)
        githubrepo_rv?.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                githubrepo_rv?.layoutManager.get
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
        githubrepo_rv.adapter = GitRepoAdapter(gitRepos as ArrayList<DataGitRepo>)
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.start()
    }

    override fun setPresenter(presenter: GitRepoContract.Presenter) {
        mPresenter = checkNotNull(presenter)
    }


}