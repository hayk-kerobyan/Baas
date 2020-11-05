package com.virtussoft.demo.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()


    @Singleton
    @Provides
    fun provideFirebaseFirestoreSettings() = firestoreSettings {
        isPersistenceEnabled = false
        isSslEnabled = true
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(
        settings: FirebaseFirestoreSettings
    ) = FirebaseFirestore.getInstance().apply { firestoreSettings = settings }
}