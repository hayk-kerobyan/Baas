package com.virtussoft.demo.model.user.dto

import com.virtussoft.demo.model.user.dto.db.UserLocalRepo
import com.virtussoft.demo.model.user.dto.network.UserRemoteRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val localRepo: UserLocalRepo,
    private val remoteRepo: UserRemoteRepo
){

}