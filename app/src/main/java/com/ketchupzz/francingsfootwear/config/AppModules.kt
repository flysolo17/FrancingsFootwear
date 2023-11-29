package com.ketchupzz.francingsfootwear.config

import com.ketchupzz.francingsfootwear.repository.AuthRepository
import com.ketchupzz.francingsfootwear.repository.AuthRepositoryImpl
import com.ketchupzz.francingsfootwear.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return ApiInstance.api.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(authService)
    }
}