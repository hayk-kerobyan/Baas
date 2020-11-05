package com.virtussoft.demo.model.user.dto

import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.model.user.dto.db.UserDb
import com.virtussoft.demo.model.user.dto.net.UserNet


internal fun User.toRemoteDto(
    id: String? = null,
    avatarUrl: String? = null
) = UserNet(
    id = id ?: this.id,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatarUrl ?: this.avatarUrl
)

internal fun UserDb.toDomain() = User(
    id = id,
    databaseId = databaseId,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatarUrl
)

internal fun UserNet.toLocalDto() = UserDb(
    id = id.orEmpty(),
    firstName = firstName.orEmpty(),
    lastName = lastName.orEmpty(),
    avatarUrl = avatarUrl.orEmpty()
)