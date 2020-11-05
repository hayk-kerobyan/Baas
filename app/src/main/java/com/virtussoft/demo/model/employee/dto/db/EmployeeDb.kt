package com.virtussoft.demo.model.employee.dto.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeDb(
    @PrimaryKey(autoGenerate = true) val databaseId: Long = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "companyId") val companyId: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String,
    @ColumnInfo(name = "companyName") val companyName: String
)