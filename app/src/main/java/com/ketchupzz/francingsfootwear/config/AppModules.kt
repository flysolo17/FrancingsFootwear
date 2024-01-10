package com.ketchupzz.francingsfootwear.config


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwear.repository.auth.AuthRepository
import com.ketchupzz.francingsfootwear.repository.auth.AuthRepositoryImpl
import com.ketchupzz.francingsfootwear.repository.cart.CartRepository
import com.ketchupzz.francingsfootwear.repository.cart.CartRepositoryImpl
import com.ketchupzz.francingsfootwear.repository.messages.MessagesRepository
import com.ketchupzz.francingsfootwear.repository.messages.MessagesRepositoryImpl
import com.ketchupzz.francingsfootwear.repository.products.ProductRepository
import com.ketchupzz.francingsfootwear.repository.products.ProductRepositoryImpl
import com.ketchupzz.francingsfootwear.repository.transaction.TransactionRepository
import com.ketchupzz.francingsfootwear.repository.transaction.TransactionRepositoryImpl
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
    fun providesAuthRepository(firestore: FirebaseFirestore,firebaseAuth: FirebaseAuth,storage: FirebaseStorage) : AuthRepository {
        return AuthRepositoryImpl(firestore,firebaseAuth,storage)
    }
    @Singleton
    @Provides
    fun provideProductRepository(firestore: FirebaseFirestore) : ProductRepository {
        return  ProductRepositoryImpl(firestore)
    }
    @Singleton
    @Provides
    fun provideCartRepository(firestore: FirebaseFirestore) : CartRepository {
        return CartRepositoryImpl(firestore)
    }

    @Singleton
    @Provides
    fun provideTransactionRepository(firestore: FirebaseFirestore,storage: FirebaseStorage) : TransactionRepository {
        return TransactionRepositoryImpl(firestore,storage)
    }

    @Singleton
    @Provides
    fun provideMessageRepository(firestore: FirebaseFirestore) : MessagesRepository {
        return MessagesRepositoryImpl(firestore)
    }
}