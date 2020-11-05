package com.virtussoft.demo.model.company.dto.net

import android.net.Uri
import com.virtussoft.demo.model.company.Company
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyRemoteRepo @Inject constructor(
    private val companyApi: CompanyApi
) {

    internal suspend fun createCompany(company: Company) = companyApi.createCompany(company)

    internal suspend fun getCompany(id: Long) = companyApi.getCompany(id)

    internal suspend fun getCompanies(since: String, count: Int): List<CompanyNet> =
        companyApi.getCompanies(since, count)

}