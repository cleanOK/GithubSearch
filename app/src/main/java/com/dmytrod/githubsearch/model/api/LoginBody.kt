package com.dmytrod.githubsearch.model.api

import com.google.gson.annotations.SerializedName

data class LoginBody(
        @SerializedName("client_id")
        val clientId: String,
        @SerializedName("client_secret")
        val clientSecret: String,
        @SerializedName("code")
        val code: String
)