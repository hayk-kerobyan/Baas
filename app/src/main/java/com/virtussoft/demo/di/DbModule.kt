package com.virtussoft.demo.di

import android.content.Context
import androidx.room.Room
import com.virtussoft.demo.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideCompanyDao(database: AppDatabase) = database.companyDao()

    @Singleton
    @Provides
    fun provideEmployeeDao(database: AppDatabase) = database.employeeDao()

}