package com.example.githubbrowser

import androidx.paging.ItemKeyedDataSource
import com.example.githubbrowser.model.RepositoryDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class ReposDataSource(
    private val reposListInteractor: ReposListInteractor,
    private val scope: CoroutineScope,
    private val errorCallback: () -> Unit = { }
) : ItemKeyedDataSource<Long, RepositoryDto>() {
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<RepositoryDto>
    ) {
        scope.launch {
            reposListInteractor.getReposList().collect { result ->
                if (result.data != null) {
                    callback.onResult(result.data, 0, result.data.size)
                } else {
                    errorCallback()
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<RepositoryDto>) {
        scope.launch {
            reposListInteractor.getReposList(params.key).collect { result ->
                if (result.data != null) {
                    callback.onResult(result.data)
                } else {
                    errorCallback
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<RepositoryDto>) {
        /* no-op */
    }

    override fun getKey(item: RepositoryDto) = item.id
}