package com.dmeetyxc.lingolearn.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dmeetyxc.lingolearn.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "database"
    )
        .build()

    @Provides
    fun provideDatabaseDao(appDatabase: AppDatabase) = appDatabase.bookDao()

    @Provides
    fun provideDictionaryDao(appDatabase: AppDatabase) = appDatabase.dictionaryDao()

    @Provides
    fun provideTextDataDao(appDatabase: AppDatabase) = appDatabase.textDataDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
}