package com.yucox.pockettorocketto.di

import com.google.firebase.database.FirebaseDatabase
import com.yucox.pockettorocketto.Model.Favorite
import com.yucox.pockettorocketto.Repository.FavoriteRepository
import com.yucox.pockettorocketto.Repository.FirebaseRepository
import com.yucox.pockettorocketto.Repository.IFavoriteRepository
import com.yucox.pockettorocketto.Repository.IFirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            setOf(Favorite::class)
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideFirebase(): FirebaseDatabase {
        val database = FirebaseDatabase.getInstance()
        return database
    }

    @Singleton
    @Provides
    fun provideIFavoriteRepository(realm: Realm): IFavoriteRepository {
        return FavoriteRepository(realm)
    }

    @Singleton
    @Provides
    fun provideIAppRepository(firebaseDatabase: FirebaseDatabase): IFirebaseRepository {
        return FirebaseRepository(firebaseDatabase)
    }
}