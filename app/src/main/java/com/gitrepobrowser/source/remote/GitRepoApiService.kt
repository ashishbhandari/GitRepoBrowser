package com.gitrepobrowser.source.remote

import com.gitrepobrowser.BuildConfig
import com.gitrepobrowser.source.entities.DataGitRepo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


/**
 * @author ashish
 */
interface GitRepoApiService {


    @retrofit2.http.GET("users/JakeWharton/repos")
    fun loadGitRepos(@retrofit2.http.Query("client_id") clientId: String,
                     @retrofit2.http.Query("client_secret") clientSecret: String,
                     @retrofit2.http.Query("page") page: Int,
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

            val okClientBuilder = OkHttpClient.Builder()

            okClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            okClientBuilder.addInterceptor(Interceptor { chain: Interceptor.Chain? ->
                val original = chain?.request()

                val request = original?.newBuilder()?.header("User-Agent", "GitRepoBrowser")
                        ?.header("Accept", "application/vnd.github.v3+json")
                        ?.method(original.method(), original.body())
                        ?.build()

                return@Interceptor chain?.proceed(request)

            })

            return okClientBuilder.build()
        }
    }


}