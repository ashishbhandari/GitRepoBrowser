package com.gitrepobrowser.home

import com.gitrepobrowser.source.entities.DataGitRepo

/**
 * @author ashish
 */
interface BaseViewHolder {

    fun bindViews(gitRepo: DataGitRepo)

}