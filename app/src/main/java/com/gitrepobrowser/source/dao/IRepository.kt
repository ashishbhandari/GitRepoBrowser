package com.gitrepobrowser.source.dao

/**
 * @author ashish
 */
interface IRepository {

    fun <T> add(item: T)
    fun <T> update(item: T)
    //other crud methods go here
}