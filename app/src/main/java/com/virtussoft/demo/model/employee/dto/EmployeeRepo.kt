package com.virtussoft.demo.model.employee.dto

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.employee.dto.db.EmployeeLocalRepo
import com.virtussoft.demo.model.employee.dto.net.EmployeeRemoteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepo @Inject constructor(
    private val mediator: EmployeeRemoteMediator,
    private val localRepo: EmployeeLocalRepo,
    private val remoteRepo: EmployeeRemoteRepo
) {

    companion object {
        private const val PAGE_SIZE = 10
        private const val INITIAL_KEY = 0
    }

    suspend fun createEmployee(employee: Employee): Employee {
        val newEmployee = remoteRepo.createEmployee(employee)
        localRepo.insert(newEmployee.toLocalDto())
        return localRepo.getById(newEmployee.id!!).toDomain()
    }

    fun getEmployees(companyId: String): Flow<PagingData<Employee>> {
        mediator.setCompanyId(companyId)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = mediator,
            initialKey = INITIAL_KEY
        ) {
            localRepo.getByPortions(companyId)
        }.flow.map { it.map { it.toDomain() } }
    }
}