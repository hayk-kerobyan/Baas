package com.virtussoft.demo.model.employee.dto.net

import com.virtussoft.demo.model.employee.Employee
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRemoteRepo @Inject constructor(
    private val employeeApi: EmployeeApi
) {

    internal suspend fun createEmployee(employee: Employee) = employeeApi.createEmployee(employee)

    internal suspend fun getEmployee(id: Long) = employeeApi.getEmployee(id)

    internal suspend fun getEmployees(companyId:String, since: String, count: Int) =
        employeeApi.getEmployees(companyId, since, count)

}