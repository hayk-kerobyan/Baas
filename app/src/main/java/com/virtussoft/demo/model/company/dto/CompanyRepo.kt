package com.virtussoft.demo.model.company.dto

import android.net.Uri
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.company.dto.db.CompanyLocalRepo
import com.virtussoft.demo.model.company.dto.net.CompanyRemoteRepo
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyRepo @Inject constructor(
    private val mediator: CompanyRemoteMediator,
    private val localRepo: CompanyLocalRepo,
    private val remoteRepo: CompanyRemoteRepo
) {

    companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_KEY = 0
    }

    suspend fun createCompany(company: Company) = remoteRepo.createCompany(company)

    fun getCompanyByDbId(databaseId:Long) = Transformations.map(localRepo.getById(databaseId)){
        it.toDomain()
    }

    fun getCompanies() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        remoteMediator = mediator,
        initialKey = INITIAL_KEY
    ) {
        localRepo.getByPortions()
    }.flow.map { it.map { it.toDomain() } }
}