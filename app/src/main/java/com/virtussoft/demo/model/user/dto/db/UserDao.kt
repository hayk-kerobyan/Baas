package com.virtussoft.demo.model.user.dto.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users:List<UserDb>)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("SELECT * FROM users WHERE databaseId = :databaseId")
    fun getById(databaseId: Long): LiveData<UserDb>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getByPortions(): PagingSource<Int, UserDb>

    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<UserDb>>
}