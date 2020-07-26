package com.example.githubbrowser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import com.example.githubbrowser.GithubApi
import com.example.githubbrowser.RepoInfoRepository
import com.example.githubbrowser.RepoNavigateIntent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class RepoInfoViewModel : ViewModel() {
    private val _lastCommitLiveData = MutableLiveData<CommitInfo>()
    val lastCommitLiveData: LiveData<CommitInfo> get() = _lastCommitLiveData

    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
    private val repository = RepoInfoRepository(GithubApi.retrofit().create(GithubApi::class.java))
    private val baseUrl = "https://api.github.com"

    fun init(navigateIntent: RepoNavigateIntent) {
        getCommitInfo(navigateIntent.commitUrl)
    }

    private fun getCommitInfo(commitsUrl: String) {
        val commitsUrlClean = commitsUrl.replace(baseUrl, "").replace("{/sha}", "")
        viewModelScope.launch {
            repository.getCommitInfo(commitsUrlClean).collect {
                if (it.data != null) {
                    val commit = it.data[0]
                    _lastCommitLiveData.postValue(
                        CommitInfo(
                            commit.commit.message,
                            commit.commit.author.name,
                            commit.commit.author.date?.let { formatter.format(it) } ?: "",
                            commit.parents.map { it.sha }
                        )
                    )
                } else {
                    _lastCommitLiveData.postValue(
                        CommitInfo(
                            "error",
                            "error",
                            "error",
                            emptyList()
                        )
                    )
                }
            }
        }
    }
}

data class CommitInfo(
    val msg: String,
    val author: String,
    val date: String,
    val parents: List<String>
)