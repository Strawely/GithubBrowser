package com.example.githubbrowser

import androidx.paging.ItemKeyedDataSource
import com.example.githubbrowser.model.RepositoryDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReposDataSource(
    private val reposListInteractor: ReposListInteractor,
    private val scope: CoroutineScope
) : ItemKeyedDataSource<Long, RepositoryDto>() {
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<RepositoryDto>
    ) {
        scope.launch {
            reposListInteractor.getReposList().collect { result ->
                result?.let { callback.onResult(it, 0, it.size) }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<RepositoryDto>) {
        scope.launch {
            reposListInteractor.getReposList(params.key).collect { result ->
                result?.let { callback.onResult(it) }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<RepositoryDto>) {
        /* no-op */
    }

    override fun getKey(item: RepositoryDto) = item.id
}