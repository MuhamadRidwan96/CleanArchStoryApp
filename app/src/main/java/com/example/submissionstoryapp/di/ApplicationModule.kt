package com.example.submissionstoryapp.di

import com.example.submissionstoryapp.data.remote.api.ApiHelper
import com.example.submissionstoryapp.data.remote.api.ApiHelperImpl
import com.example.submissionstoryapp.data.remote.api.ApiService
import com.example.submissionstoryapp.data.repository.Repository
import com.example.submissionstoryapp.data.repository.RepositoryImpl
import com.example.submissionstoryapp.domain.StoriesUseCase
import com.example.submissionstoryapp.domain.authUseCase.LoginUseCase
import com.example.submissionstoryapp.domain.authUseCase.RegisterUseCase
import com.example.submissionstoryapp.presentation.base.ViewModelFactory
import com.example.submissionstoryapp.utils.UserPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideApiHelperImpl(apiService: ApiService): ApiHelper {
        return ApiHelperImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideRepository(apiHelper: ApiHelper):Repository{
        return RepositoryImpl(apiHelper)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(repository: Repository):ViewModelFactory{
        return ViewModelFactory(repository)
    }

    @Singleton
    @Provides
    fun provideLoginUseCase(repository: Repository,userPref:UserPref): LoginUseCase {
        return LoginUseCase(repository,userPref)
    }

    @Singleton
    @Provides
    fun provideRegisterUseCase(repository: Repository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideStoriesUseCase(repository: Repository): StoriesUseCase {
        return StoriesUseCase(repository)
    }
}