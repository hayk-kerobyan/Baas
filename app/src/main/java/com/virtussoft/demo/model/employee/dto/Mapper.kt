package com.virtussoft.demo.model.employee.dto

import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.employee.dto.db.EmployeeDb
import com.virtussoft.demo.model.employee.dto.net.EmployeeNet


internal fun Employee.toRemoteDto(
    id: String? = null
) = EmployeeNet(
    id = id ?: this.id,
    userId = userId,
    companyId = companyId,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatarUrl,
    companyName = companyName
)

internal fun EmployeeDb.toDomain() = Employee(
    id = id,
    databaseId = databaseId,
    userId = userId,
    companyId = companyId,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatarUrl,
    companyName = companyName
)

internal fun EmployeeNet.toLocalDto() = EmployeeDb(
    id = id.orEmpty(),
    userId = userId.orEmpty(),
    companyId = companyId.orEmpty(),
    firstName = firstName.orEmpty(),
    lastName = lastName.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    companyName = companyName.orEmpty()
)