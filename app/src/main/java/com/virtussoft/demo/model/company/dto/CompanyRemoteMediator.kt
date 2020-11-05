package com.virtussoft.demo.model.company.dto

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.virtussoft.demo.model.company.dto.db.CompanyDb
import com.virtussoft.demo.model.company.dto.db.CompanyLocalRepo
import com.virtussoft.demo.model.company.dto.net.CompanyRemoteRepo
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class CompanyRemoteMediator @Inject constructor(
    private val localRepo: CompanyLocalRepo,
    private val remoteRepo: CompanyRemoteRepo
) : RemoteMediator<Int, CompanyDb>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CompanyDb>): MediatorResult {
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

            val response = remoteRepo.getCompanies(since = loadKey ?: "", count = state.config.pageSize)

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