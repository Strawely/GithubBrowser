package com.example.githubbrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.githubbrowser.GithubApi
import com.example.githubbrowser.RepoNavigateIntent
import com.example.githubbrowser.ReposListDataSourceFactory
import com.example.githubbrowser.ReposListInteractor
import com.example.githubbrowser.Router

class ReposListViewModel(private val router: Router) : ViewModel() {
    private val pagingConfig = Config(50)

    val reposDataSource = ReposListDataSourceFactory(
        ReposListInteractor(
            GithubApi.retrofit()
                .create(GithubApi::class.java)
        ), viewModelScope
    ).toLiveData(
        pagingConfig
    )

    fun onItemClick(navigateIntent: RepoNavigateIntent) {
        router.navigateRepoInfo(navigateIntent)
    }
}