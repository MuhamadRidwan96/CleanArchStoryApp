package com.example.submissionstoryapp.di


import android.content.Context
import com.example.submissionstoryapp.data.Repository
import com.example.submissionstoryapp.data.RepositoryImpl
import com.example.submissionstoryapp.data.api.ApiHelper
import com.example.submissionstoryapp.data.api.ApiHelperImpl
import com.example.submissionstoryapp.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(300, TimeUnit.MILLISECONDS)
            .readTimeout(300, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiHelperImpl(apiHelperImpl: ApiHelperImpl): ApiHelper {
        return apiHelperImpl
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(repository: RepositoryImpl):ViewModelFactory{
        return repository
    }

    @Singleton
    @Provides
    fun provideGetData(repository: Repository):GetDataUseCase{
        return repository
    }

    @Singleton
    @Provides
    fun provideGetLoginUseCase(repository:Repository):GetLoginUseCase{
        return repository
    }


}