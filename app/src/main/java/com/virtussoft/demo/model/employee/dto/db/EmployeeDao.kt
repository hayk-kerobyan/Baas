package com.virtussoft.demo.model.employee.dto.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: EmployeeDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employees: List<EmployeeDb>)

    @Query("DELETE FROM employees")
    suspend fun deleteAll()

    @Query("SELECT * FROM employees WHERE id = :id")
    fun getById(id: String): EmployeeDb

    @Query("SELECT * FROM employees WHERE databaseId = :databaseId")
    fun getByDatabaseId(databaseId: Long): LiveData<EmployeeDb>

    @Query("SELECT * FROM employees WHERE companyId = :companyId ORDER BY id ASC")
    fun getByPortions(companyId: String): PagingSource<Int, EmployeeDb>

    @Query("SELECT * FROM employees")
    fun getAll(): LiveData<List<EmployeeDb>>
}