package com.example.githubbrowser.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryDto(
    val id: Long,
    val name: String,
    val owner: RepoOwner,
    @Json(name = "commits_url")
    val commitsUrl: String
)

@JsonClass(generateAdapter = true)
data class RepoOwner(
    val id: Long,
    @Json(name = "avatar_url") // todo moshi naming rule
    val avatarUrl: String,
    val login: String
)