package com.virtussoft.demo.model.employee

/**
 * Domain object
 */
data class Employee(
    val id: String = "",
    val databaseId: Long = -1L,
    val userId: String,
    val companyId: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val companyName: String
)