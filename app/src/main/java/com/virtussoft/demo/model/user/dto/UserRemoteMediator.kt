package com.virtussoft.demo.model.user.dto

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.virtussoft.demo.model.user.dto.db.UserDb
import com.virtussoft.demo.model.user.dto.db.UserLocalRepo
import com.virtussoft.demo.model.user.dto.net.UserRemoteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator @Inject constructor(
    private val localRepo: UserLocalRepo,
    private val remoteRepo: UserRemoteRepo
) : RemoteMediator<Int, UserDb>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserDb>): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.id ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were    loaded after the initial REFRESH and there are
                    // no more items to load.

                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = remoteRepo.getUsers(since = loadKey ?: "", count = state.config.pageSize)

            if (loadType == LoadType.REFRESH) localRepo.deleteAll()

            // Insert new users into database, which invalidates the
            // current PagingData, allowing Paging to present the updates
            // in the DB.
            localRepo.insert(response.map { it.toLocalDto() })

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}