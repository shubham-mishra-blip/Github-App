package com.battlebucks.battlebucksgithubapp.data.network.model.response

import com.squareup.moshi.Json

data class GithubUserDto(
    val login: String,
    @Json(name = "avatar_url") val avatarUrl: String?,
    val bio: String?,
    @Json(name = "followers") val followersCount: Int,
    @Json(name = "public_repos") val publicRepos: Int
)

data class GithubRepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "forks_count") val forks: Int,
    @Json(name = "language") val language: String?
)