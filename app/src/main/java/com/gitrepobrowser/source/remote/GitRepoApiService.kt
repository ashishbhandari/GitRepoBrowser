package com.gitrepobrowser.source.remote

import com.gitrepobrowser.source.entities.DataGitRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author ashish
 */
interface GitRepoApiService {


    @retrofit2.http.GET("users/JakeWharton/repos")
    fun loadGitRepos(@retrofit2.http.Query("page") page: Int,
                     @retrofit2.http.Query("per_page") perPage: Int): io.reactivex.Observable<List<DataGitRepo>>

    /**
     * Companion object for the factory
     */
    companion object Factory {
        fun create(): GitRepoApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .baseUrl("https://api.github.com/")
                    .build()

            return retrofit.create(GitRepoApiService::class.java)
        }

        private fun getOkHttpClient(): OkHttpClient? {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val okClientBuilder = OkHttpClient.Builder()

            okClientBuilder.addInterceptor(logging)

            return okClientBuilder.build()
        }
    }


}