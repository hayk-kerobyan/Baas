package com.virtussoft.demo.model.company.dto.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyLocalRepo @Inject constructor(
    private val companyDao: CompanyDao
) {
    suspend fun insert(companies: List<CompanyDb>) = companyDao.insert(companies)

    suspend fun deleteAll() = companyDao.deleteAll()

    fun getById(databaseId: Long) = companyDao.getById(databaseId)

    fun getByPortions() = companyDao.getByPortions()

    fun getAll() = companyDao.getAll()

}