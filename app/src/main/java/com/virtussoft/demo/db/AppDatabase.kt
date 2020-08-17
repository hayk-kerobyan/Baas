package com.virtussoft.demo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtussoft.demo.model.user.dto.db.UserDao
import com.virtussoft.demo.model.user.dto.db.UserDb

@Database(entities = [UserDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        const val NAME = "AppDb"
    }

    abstract fun userDao(): UserDao
}