package com.gitrepobrowser.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * @author ashish
 *
 * This class provides methods to help Activities to load fragment.
 *
 */
object ActivityUtils {

    fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int) {

        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()

    }

}