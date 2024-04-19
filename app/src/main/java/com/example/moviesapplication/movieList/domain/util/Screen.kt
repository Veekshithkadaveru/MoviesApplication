package com.example.moviesapplication.movieList.domain.util

sealed class Screen(val rout:String)
{
    object Home:Screen("main")
    object PopularMovieList:Screen("popularMovie")
    object UpomingMovieList:Screen("upcomingMovie")
    object Details:Screen("details")
}