package com.virtussoft.demo.model.user.dto.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteRepo @Inject constructor(
    private val userApi: UserApi
) {

}