package com.virtussoft.demo.model.employee.dto.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeLocalRepo @Inject constructor(
    private val employeeDao: EmployeeDao
) {
    suspend fun insert(employee: EmployeeDb) = employeeDao.insert(employee)

    suspend fun insert(employees: List<EmployeeDb>) = employeeDao.insert(employees)

    suspend fun deleteAll() = employeeDao.deleteAll()

    fun getById(id: String) = employeeDao.getById(id)

    fun getByPortions(companyId: String) = employeeDao.getByPortions(companyId)

    fun getAll() = employeeDao.getAll()

}