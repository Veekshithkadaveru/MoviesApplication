package com.example.moviesapplication.movieList.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.moviesapplication.movieList.data.local.movie.MovieDatabase
import com.example.moviesapplication.movieList.data.mappers.toMovie
import com.example.moviesapplication.movieList.data.mappers.toMovieEntity
import com.example.moviesapplication.movieList.data.remote.MovieApi
import com.example.moviesapplication.movieList.domain.model.Movie
import com.example.moviesapplication.movieList.domain.repository.MovieListRepository
import com.example.moviesapplication.movieList.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote
            if (shouldLoadLocalMovie) {
                emit(
                    Resource.Success(
                        data = localMovieList.map { movieEntity ->
                            movieEntity.toMovie(category)
                        }
                    )
                )
                emit(Resource.Loading(false))
                return@flow

            }
            val movieListFromAPI = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }
            catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }
            val movieEntites=movieListFromAPI.results.let {
                it.map { 
                    movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }
            movieDatabase.movieDao.upsertMovieList(movieEntites)
            emit(Resource.Success(
                movieEntites.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity=movieDatabase.movieDao.getMovieById(id)
            if (movieEntity!=null){
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category))
                )
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such movie"))
            emit(Resource.Loading(false))

        }
    }


}