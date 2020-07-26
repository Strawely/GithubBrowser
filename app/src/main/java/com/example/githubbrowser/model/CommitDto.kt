package com.example.githubbrowser.model

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class CommitDto(
    val commit: CommitInfoDto,
    val parents: List<CommitParent>
)

@JsonClass(generateAdapter = true)
data class CommitInfoDto(
    val message: String,
    val author: CommitAuthor
)

@JsonClass(generateAdapter = true)
data class CommitAuthor(
    val name: String,
    val date: Date?
)

@JsonClass(generateAdapter = true)
data class CommitParent(
    val sha: String
)