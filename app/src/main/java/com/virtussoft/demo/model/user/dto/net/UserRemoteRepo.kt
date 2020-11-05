package com.virtussoft.demo.model.user.dto.net

import android.net.Uri
import com.virtussoft.demo.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteRepo @Inject constructor(
    private val userApi: UserApi
) {

    internal suspend fun createUser(user: User, avatar: Uri?) = userApi.createUser(user, avatar)

    internal suspend fun getUser(id: Long) = userApi.getUser(id)

    internal suspend fun getUsers(since: String, count: Int): List<UserNet> =
        userApi.getUsers(since, count)

}