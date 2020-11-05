package com.virtussoft.demo.model.employee.dto.net

/**
 *
 * Remote DTO representation of [com.virtussoft.demo.model.company.Company] Domain object,
 * which intentionally includes all the fields supported by API. Unnecessary fields
 * are dropped during mapping to DB DTO
 *
 * Data transformation map: Net -> Db -> Domain -> Db & Net
 * @see https://proandroiddev.com/the-real-repository-pattern-in-android-efba8662b754
 *
 */
data class EmployeeNet(
    val id: String? = null,
    val userId: String? = null,
    val companyId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatarUrl: String? = null,
    val companyName: String? = null
)
