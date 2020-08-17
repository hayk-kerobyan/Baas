package com.virtussoft.demo.model.user.dto.network

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

/**
 *
 * Remote DTO representation of [com.virtussoft.demo.model.user.User] Domain object,
 * which intentionally includes all the fields supported by API. Unnecessary fields
 * are dropped during mapping to DB DTO
 *
 * Data transformation map: Net -> Db -> Domain -> Db & Net
 * @see https://proandroiddev.com/the-real-repository-pattern-in-android-efba8662b754
 *
 */
data class UserNet(
    @SerializedName("login") @Expose val login: String? = null,
    @SerializedName("id") @Expose val id: Long? = null,
    @SerializedName("node_id") @Expose val nodeId: String? = null,
    @SerializedName("avatar_url") @Expose val avatarUrl: String? = null,
    @SerializedName("gravatar_id") @Expose val gravatarId: String? = null,
    @SerializedName("url") @Expose val url: String? = null,
    @SerializedName("html_url") @Expose val htmlUrl: String? = null,
    @SerializedName("followers_url") @Expose val followersUrl: String? = null,
    @SerializedName("following_url") @Expose val followingUrl: String? = null,
    @SerializedName("gists_url") @Expose val gistsUrl: String? = null,
    @SerializedName("starred_url") @Expose val starredUrl: String? = null,
    @SerializedName("subscriptions_url") @Expose val subscriptionsUrl: String? = null,
    @SerializedName("organizations_url") @Expose val organizationsUrl: String? = null,
    @SerializedName("repos_url") @Expose val reposUrl: String? = null,
    @SerializedName("events_url") @Expose val eventsUrl: String? = null,
    @SerializedName("received_events_url") @Expose val receivedEventsUrl: String? = null,
    @SerializedName("type") @Expose val type: String? = null,
    @SerializedName("site_admin") @Expose val siteAdmin: Boolean? = null
)
