package com.virtussoft.demo.model.user.dto.network

import com.virtussoft.demo.model.user.User

internal fun User.toRemoteDto() = UserNet(
    id = id,
    login = login,
    nodeId = nodeId,
    avatarUrl = avatarUrl
)