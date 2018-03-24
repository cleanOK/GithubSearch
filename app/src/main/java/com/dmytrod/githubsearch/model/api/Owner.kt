package com.dmytrod.githubsearch.model.api

import com.google.gson.annotations.SerializedName

data class Owner(

        @SerializedName("avatar_url")
        private val mAvatarUrl: String,
        @SerializedName("gravatar_id")
        private val mGravatarId: String,
        @SerializedName("id")
        private val mId: Long,
        @SerializedName("login")
        private val mLogin: String,
        @SerializedName("received_events_url")
        private val mReceivedEventsUrl: String,
        @SerializedName("type")
        private val mType: String,
        @SerializedName("url")
        private val mUrl: String
)
