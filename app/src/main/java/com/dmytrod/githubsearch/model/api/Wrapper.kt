package com.dmytrod.githubsearch.model.api

import com.google.gson.annotations.SerializedName

data class Wrapper(

        @SerializedName("incomplete_results")
        var incompleteResults: Boolean,
        @SerializedName("items")
        var items: List<RepositoryResponse>,
        @SerializedName("total_count")
        var totalCount: Long
)