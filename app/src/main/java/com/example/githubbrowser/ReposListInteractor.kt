package com.example.githubbrowser

import kotlinx.coroutines.flow.flow
import org.intellij.lang.annotations.Flow
import timber.log.Timber
import java.lang.Exception

class ReposListInteractor(private val api: GithubApi) {
    fun getReposList(since: Long? = null) = flow {
        try {
            emit(api.getReposList(since ?: 0))
        } catch (e: Exception) {
            Timber.e(e)
            emit(null)
        }
    }
}