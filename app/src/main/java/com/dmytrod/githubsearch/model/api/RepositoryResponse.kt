package com.dmytrod.githubsearch.model.api

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(

        @SerializedName("description")
        var description: String,
        @SerializedName("full_name")
        var fullName: String,
        @SerializedName("homepage")
        var homepage: String,
        @SerializedName("html_url")
        var htmlUrl: String,
        @SerializedName("id")
        var id: Long,
        @SerializedName("name")
        var name: String,
        @SerializedName("owner")
        var owner: Owner,
        @SerializedName("score")
        var score: Double,
        @SerializedName("size")
        var size: Long,
        @SerializedName("stargazers_count")
        var stargazersCount: Long,
        @SerializedName("url")
        var url: String,
        @SerializedName("watchers_count")
        var watchersCount: Long
)
