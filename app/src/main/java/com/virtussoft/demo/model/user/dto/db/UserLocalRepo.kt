package com.virtussoft.demo.model.user.dto.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalRepo @Inject constructor(
    private val userDao: UserDao
){

}