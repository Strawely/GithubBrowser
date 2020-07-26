package com.example.githubbrowser

import androidx.paging.DataSource
import com.example.githubbrowser.model.RepositoryDto
import kotlinx.coroutines.CoroutineScope

class ReposListDataSourceFactory(
    private val reposListInteractor: ReposListInteractor,
    private val scope: CoroutineScope
) : DataSource.Factory<Long, RepositoryDto>() {
    override fun create(): DataSource<Long, RepositoryDto> {
        return ReposDataSource(reposListInteractor, scope)
    }
}