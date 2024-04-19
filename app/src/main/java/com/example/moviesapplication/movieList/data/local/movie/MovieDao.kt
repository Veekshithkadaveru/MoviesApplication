package com.example.moviesapplication.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(MovieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id=:id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category=:category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}