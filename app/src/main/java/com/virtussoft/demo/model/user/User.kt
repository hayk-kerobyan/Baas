package com.virtussoft.demo.model.user

/**
 * Domain object
 */
data class User(
    val id: String,
    val databaseId: Long,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String
)