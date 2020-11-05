package com.virtussoft.demo.model.user.dto.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalRepo @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insert(users: List<UserDb>) = userDao.insert(users)

    suspend fun deleteAll() = userDao.deleteAll()

    fun getByPortions() = userDao.getByPortions()

    fun getAll() = userDao.getAll()

}