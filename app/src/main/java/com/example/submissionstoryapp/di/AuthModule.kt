package com.example.submissionstoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.submissionstoryapp.utils.AuthInterceptor
import com.example.submissionstoryapp.utils.UserPref
import com.example.submissionstoryapp.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {


    @Singleton
    @Provides
    fun provideAuthInterceptor(userPref: UserPref): AuthInterceptor {
        return AuthInterceptor(userPref)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserPref(dataStore: DataStore<Preferences>): UserPref {
        return UserPref(dataStore)
    }

}