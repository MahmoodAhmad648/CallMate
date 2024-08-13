package com.mahmood.callmate.data.di

import android.app.Application
import androidx.room.Room
import com.mahmood.callmate.data.database.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Provides
    fun provideDatabase(application: Application): ContactDatabase {
        return Room.databaseBuilder(
            application,
            ContactDatabase::class.java,
            "contacts_db"
        ).fallbackToDestructiveMigration().build()
    }

}