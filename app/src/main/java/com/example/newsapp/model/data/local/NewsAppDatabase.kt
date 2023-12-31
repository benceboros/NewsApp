package com.example.newsapp.model.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.model.data.local.daos.NewsDao
import com.example.newsapp.model.data.local.entities.NewsEntity

@Database(
    entities = [NewsEntity::class],
    version = 2
)

abstract class NewsAppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}