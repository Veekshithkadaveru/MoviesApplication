package com.example.moviesapplication.details.presentation

import com.example.moviesapplication.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
