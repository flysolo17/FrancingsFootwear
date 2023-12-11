package com.ketchupzz.francingsfootwear.config


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzz.francingsfootwear.repository.AuthRepository
import com.ketchupzz.francingsfootwear.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Singleton
    @Provides
    fun providesAuthRepository() : AuthRepository {
        return AuthRepositoryImpl(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance())
    }
}