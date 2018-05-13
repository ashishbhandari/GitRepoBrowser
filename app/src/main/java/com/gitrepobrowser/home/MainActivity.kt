package com.gitrepobrowser.home

import android.os.Bundle
import com.gitrepobrowser.BaseActivity
import com.gitrepobrowser.R
import com.gitrepobrowser.source.GitSourceRepoProvider
import com.gitrepobrowser.util.ActivityUtils

/**
 * @author ashish
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var homeFragment: HomeFragment? = supportFragmentManager.findFragmentById(R.id.home_content) as? HomeFragment
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = HomeFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, homeFragment!!, R.id.home_content)
        }

        HomePresenter(GitSourceRepoProvider.provideGitRepoRepository(applicationContext), homeFragment)

    }
}
