package com.gitrepobrowser.home

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gitrepobrowser.R
import com.gitrepobrowser.source.entities.DataGitRepo
import kotlinx.android.synthetic.main.git_repo_list_item.view.*


/**
 * @author ashish
 */
class GitRepoAdapter(private var dataGitRepos: ArrayList<DataGitRepo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }

    private var isLoadingAdded: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent?.context)

        if (viewType == VIEW_TYPE_ITEM) {
            val view = inflater.inflate(R.layout.git_repo_list_item, parent, false)
            return RepoHolder(view)
        } else {
            val view = inflater.inflate(R.layout.git_repo_foter_list_item, parent, false)
            return LoaderViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {

        return if (position == dataGitRepos.size - 1 && isLoadingAdded) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        if (dataGitRepos != null) {
            return dataGitRepos.size
        }
        return 0
    }

    private fun addGitRepoItem(gitRepo: DataGitRepo) {
        dataGitRepos.add(gitRepo)
        notifyItemInserted(dataGitRepos.size - 1)
    }

    fun removeGitRepoItem(gitRepo: DataGitRepo) {
        val position = dataGitRepos.indexOf(gitRepo)
        if (position > -1) {
            dataGitRepos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addLoadingProgress() {
        isLoadingAdded = true
        addGitRepoItem(DataGitRepo(-1, "", "", 0))
    }

    fun removeLoadingProgress() {
        isLoadingAdded = false
        val position = dataGitRepos.size - 1
        val gitRepoItem = getGitRepoItem(position)
        if (gitRepoItem != null && gitRepoItem.id == -1L) {
            dataGitRepos.remove(gitRepoItem)
        }
    }

    private fun getGitRepoItem(index: Int): DataGitRepo {
        return dataGitRepos[index]
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        (holder as BaseViewHolder).bindViews(dataGitRepos[position])
    }


    class RepoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, BaseViewHolder {

        private var view: View = v
        private var dataGitRepo: DataGitRepo? = null

        init {
            v.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            itemView.context
        }

        override fun bindViews(gitRepo: DataGitRepo) {
            this.dataGitRepo = gitRepo
            view.tv_repo_name.text = dataGitRepo?.name
            view.tv_repo_forks.text = dataGitRepo?.full_name
        }

    }

    class LoaderViewHolder(v: View) : RecyclerView.ViewHolder(v), BaseViewHolder {
        override fun bindViews(gitRepo: DataGitRepo) {
            // not required
        }
    }
}