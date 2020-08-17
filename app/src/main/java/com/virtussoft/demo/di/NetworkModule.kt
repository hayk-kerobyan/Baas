package com.virtussoft.demo.di

import com.google.gson.Gson
import com.virtussoft.demo.BuildConfig
import com.virtussoft.demo.model.user.dto.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideClient() = OkHttpClient()

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson) = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideRetrofit(
        converter: Converter.Factory,
        client: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(converter)
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit) = retrofit.create(UserApi::class.java)
}