package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.model.data.local.NewsAppDatabase
import com.example.newsapp.model.data.local.daos.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NewsAppDatabase {
        return Room.databaseBuilder(
            context,
            NewsAppDatabase::class.java,
            "news-app-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(newsAppDatabase: NewsAppDatabase): NewsDao {
        return newsAppDatabase.newsDao()
    }
}