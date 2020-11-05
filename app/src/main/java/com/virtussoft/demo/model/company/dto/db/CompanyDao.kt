package com.virtussoft.demo.model.company.dto.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companies:List<CompanyDb>)

    @Query("DELETE FROM companies")
    suspend fun deleteAll()

    @Query("SELECT * FROM companies WHERE databaseId = :databaseId")
    fun getById(databaseId: Long): LiveData<CompanyDb>

    @Query("SELECT * FROM companies ORDER BY id ASC")
    fun getByPortions(): PagingSource<Int, CompanyDb>

    @Query("SELECT * FROM companies")
    fun getAll(): LiveData<List<CompanyDb>>
}