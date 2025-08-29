package com.battlebucks.battlebucksgithubapp.data.network

import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubRepoDto
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubUserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GithubUserDto
    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
        @Query("sort") sort: String = "updated"
    ): List<GithubRepoDto>
}