package com.battlebucks.battlebucksgithubapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.battlebucks.battlebucksgithubapp.data.network.ApiClient
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubRepoDto
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubUserDto
import com.battlebucks.battlebucksgithubapp.data.repository.paging.ReposPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class GithubRepository @Inject constructor(
    private val api: ApiClient
) {
    suspend fun getUser(username: String): GithubUserDto = api.getUser(username)

    fun getUserReposPaged(username: String): Flow<PagingData<GithubRepoDto>> =
        Pager(
            config = PagingConfig(pageSize = 30, prefetchDistance = 5, enablePlaceholders = false),
            pagingSourceFactory = { ReposPagingSource(api, username) }
        ).flow
}