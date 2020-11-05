package com.virtussoft.demo.model.user.dto.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDb(
    @PrimaryKey(autoGenerate = true) val databaseId: Long = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String
)