package com.example.githubbrowser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.githubbrowser.RepoNavigateIntent
import com.example.githubbrowser.ReposListDataSourceFactory
import com.example.githubbrowser.ReposListInteractor
import com.example.githubbrowser.Router
import timber.log.Timber
import javax.inject.Inject

class ReposListViewModel @Inject constructor(
    private val router: Router,
    interactor: ReposListInteractor
) : ViewModel() {
    private val pagingConfig = Config(50)
    val reposDataSource = ReposListDataSourceFactory(
        interactor, viewModelScope, ::errorCallback
    ).toLiveData(
        pagingConfig
    )

    private val _errorLiveData = MutableLiveData<Boolean>()
    val errorLiveData: LiveData<Boolean> get() = _errorLiveData

    fun onItemClick(navigateIntent: RepoNavigateIntent) {
        router.navigateRepoInfo(navigateIntent)
    }

    private fun errorCallback() {
        _errorLiveData.postValue(true)
    }
}