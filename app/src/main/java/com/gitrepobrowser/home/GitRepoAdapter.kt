package com.gitrepobrowser.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gitrepobrowser.R
import com.gitrepobrowser.source.entities.DataGitRepo
import kotlinx.android.synthetic.main.git_repo_list_item.view.*


/**
 * @author ashish
 */
class GitRepoAdapter(private val dataGitRepos: ArrayList<DataGitRepo>) : RecyclerView.Adapter<GitRepoAdapter.RepoHolder>() {

    override fun getItemCount(): Int {
        return dataGitRepos.size
    }


    override fun onBindViewHolder(holder: RepoHolder?, position: Int) {
        val dataGitRepo = dataGitRepos[position]
        holder?.bindPhoto(dataGitRepo)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GitRepoAdapter.RepoHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.git_repo_list_item, parent, false)
        return RepoHolder(view)
    }


    class RepoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var dataGitRepo: DataGitRepo? = null

        init {
            v.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val context = itemView.context
        }


        fun bindPhoto(dataGitRepo: DataGitRepo) {
            this.dataGitRepo = dataGitRepo
            view.tv_repo_name.text = dataGitRepo.name
            view.tv_repo_forks.text = dataGitRepo.full_name
        }


    }
}