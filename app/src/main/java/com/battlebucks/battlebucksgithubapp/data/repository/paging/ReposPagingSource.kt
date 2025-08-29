package com.battlebucks.battlebucksgithubapp.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.battlebucks.battlebucksgithubapp.data.network.ApiClient
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubRepoDto
import java.io.IOException
import retrofit2.HttpException


class ReposPagingSource(
    private val api: ApiClient,
    private val username: String
) : PagingSource<Int, GithubRepoDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepoDto> {
        val page = params.key ?: 1
        return try {
            val data = api.getRepos(username, page = page, perPage = 30)
            val nextKey = if (data.isEmpty()) null else page + 1
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e) // network failure
        } catch (e: HttpException) {
            LoadResult.Error(e) // 4xx/5xx
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepoDto>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }
}