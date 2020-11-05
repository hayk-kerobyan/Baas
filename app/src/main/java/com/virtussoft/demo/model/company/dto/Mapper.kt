package com.virtussoft.demo.model.company.dto

import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.company.dto.db.CompanyDb
import com.virtussoft.demo.model.company.dto.net.CompanyNet


internal fun Company.toRemoteDto(
    id: String? = null
) = CompanyNet(
    id = id ?: this.id,
    name = name
)

internal fun CompanyDb.toDomain() = Company(
    id = id,
    databaseId = databaseId,
    name = name
)

internal fun CompanyNet.toLocalDto() = CompanyDb(
    id = id.orEmpty(),
    name = name.orEmpty()
)