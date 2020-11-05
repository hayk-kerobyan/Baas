package com.virtussoft.demo.model.company.dto.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyDb(
    @PrimaryKey(autoGenerate = true) val databaseId: Long = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String
)