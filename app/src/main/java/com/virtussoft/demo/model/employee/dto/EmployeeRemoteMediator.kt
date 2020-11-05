package com.virtussoft.demo.model.employee.dto

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.virtussoft.demo.model.employee.dto.db.EmployeeDb
import com.virtussoft.demo.model.employee.dto.db.EmployeeLocalRepo
import com.virtussoft.demo.model.employee.dto.net.EmployeeRemoteRepo
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class EmployeeRemoteMediator @Inject constructor(
    private val localRepo: EmployeeLocalRepo,
    private val remoteRepo: EmployeeRemoteRepo
) : RemoteMediator<Int, EmployeeDb>() {

    private lateinit var companyId: String

    fun setCompanyId(companyId: String) {
        this.companyId = companyId
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EmployeeDb>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.id ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
            }

            val response = remoteRepo.getEmployees(
                companyId = companyId,
                since = loadKey ?: "",
                count = state.config.pageSize
            )

            if (loadType == LoadType.REFRESH) localRepo.deleteAll()

            localRepo.insert(response.map { it.toLocalDto() })

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}