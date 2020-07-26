package com.example.githubbrowser

import com.example.githubbrowser.model.CommitDto
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception

class RepoInfoRepository(private val api: GithubApi) {
    fun getCommitInfo(commitsUrl: String) = flow {
        try {
            val value = api.getCommitInfo(commitsUrl)
            emit(CommitsResult(value))
        } catch (e: Exception) {
            Timber.e(e)
            emit(CommitsResult(error = e))
        }
    }
}

data class CommitsResult(
    val data: List<CommitDto>? = null,
    val error: Exception? = null
)