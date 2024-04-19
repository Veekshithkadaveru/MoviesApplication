package com.example.moviesapplication.movieList.presentation

import com.example.moviesapplication.movieList.domain.repository.MovieListRepository

sealed interface MovieListUiEvent {
  data class Paginate(val category: String):MovieListUiEvent
    object Navigate:MovieListUiEvent
}