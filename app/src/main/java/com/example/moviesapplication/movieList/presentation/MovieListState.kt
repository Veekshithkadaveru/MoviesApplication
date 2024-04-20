package com.example.moviesapplication.movieList.presentation

import com.example.moviesapplication.movieList.domain.model.Movie
import com.example.moviesapplication.movieList.domain.util.Resource

data class MovieListState(
    val isLoading: Boolean = false,
    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,
    val isCurrentPopularScreen: Boolean = true,
    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList(),


    )
