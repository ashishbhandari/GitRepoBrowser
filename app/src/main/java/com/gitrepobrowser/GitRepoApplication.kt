package com.gitrepobrowser

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author ashish
 */
class GitRepoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

    }
}