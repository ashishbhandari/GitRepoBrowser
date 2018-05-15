package com.gitrepobrowser.source.dao

import com.gitrepobrowser.source.entities.DataGitRepo
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @author ashish
 */
open class GitRepo(
        @PrimaryKey var pageId: Int = 0,
        var dataGitRepos: RealmList<DataGitRepo> = RealmList()
) : RealmObject() {


}