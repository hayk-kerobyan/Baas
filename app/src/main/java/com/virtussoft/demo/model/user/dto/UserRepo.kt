package com.virtussoft.demo.model.user.dto

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.model.user.dto.db.UserLocalRepo
import com.virtussoft.demo.model.user.dto.net.UserRemoteRepo
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val mediator: UserRemoteMediator,
    private val localRepo: UserLocalRepo,
    private val remoteRepo: UserRemoteRepo
) {

    companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_KEY = 0
    }

    suspend fun createUser(user: User, avatar: Uri?) = remoteRepo.createUser(user, avatar)

    fun getUsers() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        remoteMediator = mediator,
        initialKey = INITIAL_KEY
    ) {
        localRepo.getByPortions()
    }.flow.map { it.map { it.toDomain() } }
}