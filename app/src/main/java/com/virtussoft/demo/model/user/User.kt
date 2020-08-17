package com.virtussoft.demo.model.user

data class User(
    val id: Long,
    val databaseId: Long,
    val login: String,
    val nodeId: String,
    val avatarUrl: String
)
