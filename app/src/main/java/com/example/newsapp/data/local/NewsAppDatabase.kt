package com.example.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.daos.NewsDao
import com.example.newsapp.data.local.entities.NewsEntity

@Database(
    entities = [NewsEntity::class],
    version = 1
)

abstract class NewsAppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}