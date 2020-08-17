package com.virtussoft.demo.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.virtussoft.demo.BuildConfig
import com.virtussoft.demo.db.AppDatabase
import com.virtussoft.demo.model.user.dto.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ) = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        AppDatabase.NAME
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

}