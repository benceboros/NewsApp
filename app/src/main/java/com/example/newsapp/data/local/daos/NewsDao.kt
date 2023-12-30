package com.example.newsapp.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entities.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsEntities(newsEntities: List<NewsEntity>)

    @Query("SELECT * FROM newsentity")
    suspend fun getNews(): List<NewsEntity>

    @Query("SELECT * FROM newsentity WHERE id = :id")
    suspend fun getSelectedNews(id: Int): NewsEntity

    @Query("DELETE FROM newsentity")
    suspend fun deleteNewsEntities()
}