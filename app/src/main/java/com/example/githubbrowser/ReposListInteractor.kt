package com.example.githubbrowser

import com.example.githubbrowser.model.RepositoryDto
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ReposListInteractor @Inject constructor(private val api: GithubApi) {
    fun getReposList(since: Long? = null) = flow {
        try {
            val result = api.getReposList(since ?: 0)
            emit(ReposListResult(result))
        } catch (e: Exception) {
            Timber.e(e)
            emit(ReposListResult(error = e))
        }
    }
}

data class ReposListResult(
    val data: List<RepositoryDto>? = null,
    val error: Exception? = null
)