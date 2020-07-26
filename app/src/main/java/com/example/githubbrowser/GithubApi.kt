package com.example.githubbrowser

import com.example.githubbrowser.model.CommitDto
import com.example.githubbrowser.model.RepositoryDto
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("/repositories")
    suspend fun getReposList(@Query("since") since: Long): List<RepositoryDto>

    @GET("{path}")
    suspend fun getCommitInfo(@Path("path", encoded = true) path: String): List<CommitDto>

    companion object {
        fun retrofit(): Retrofit {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
            val moshi = Moshi.Builder().add(DateAdapter()).build()
            return Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.github.com")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
    }
}