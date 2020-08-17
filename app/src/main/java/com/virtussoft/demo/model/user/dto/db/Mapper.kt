package com.virtussoft.demo.model.user.dto.db

import com.virtussoft.demo.model.user.User

internal fun UserDb.toDomain() = User(
    id = id,
    databaseId = databaseId,
    login = login,
    nodeId = nodeId,
    avatarUrl = avatarUrl
)