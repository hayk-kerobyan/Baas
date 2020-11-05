package com.virtussoft.demo.model.user.dto.net

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
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatarUrl: String? = null
)
