package com.gitrepobrowser.source.entities

import io.realm.RealmObject

/**
 * @author ashish
 */
open class DataGitRepo(var id: Long? = 0,
                       var name: String? = "",
                       var full_name: String? = "",
                       var forks: Int? = 0) : RealmObject() {

}