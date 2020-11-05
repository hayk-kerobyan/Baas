package com.virtussoft.demo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtussoft.demo.model.company.dto.db.CompanyDao
import com.virtussoft.demo.model.company.dto.db.CompanyDb
import com.virtussoft.demo.model.employee.dto.db.EmployeeDao
import com.virtussoft.demo.model.employee.dto.db.EmployeeDb
import com.virtussoft.demo.model.user.dto.db.UserDao
import com.virtussoft.demo.model.user.dto.db.UserDb

@Database(entities = [UserDb::class, CompanyDb::class, EmployeeDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        const val NAME = "AppDb"
    }

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun employeeDao(): EmployeeDao
}