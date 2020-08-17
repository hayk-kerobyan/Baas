package com.virtussoft.demo.model.user.dto.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("lego/{id}/")
    suspend fun getUser(@Path("id") id: Long): List<UserNet>

    @GET("users/")
    suspend fun getUsers(
        @Query("per_page") count: Int,
        @Query("since") since: Int
    ): List<UserNet>
}