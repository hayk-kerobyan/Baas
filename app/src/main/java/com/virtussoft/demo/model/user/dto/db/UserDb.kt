package com.virtussoft.demo.model.user.dto.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserDb(
    @PrimaryKey(autoGenerate = true) val databaseId: Long,
    @ColumnInfo(name = "id")  val id: Long,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "node_id") val nodeId: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String
)