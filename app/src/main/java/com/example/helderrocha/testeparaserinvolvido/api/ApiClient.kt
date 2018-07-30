package com.example.helderrocha.testeparaserinvolvido.api

import com.example.helderrocha.testeparaserinvolvido.model.GenreResponse
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.model.UpcomingMoviesResponse
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ApiClient @Inject constructor(
        private val tmdbApi: TmdbApi
) {
    fun genres(apiKey: String,
               language: String
    ): Single<GenreResponse> {
        return tmdbApi.genres(apiKey, language)
    }

    fun upcomingMovies(apiKey: String,
                       language: String,
                       page: Long,
                       region: String
    ): Observable<UpcomingMoviesResponse> {
        return tmdbApi.upcomingMovies(apiKey, language, page, region)
    }

    fun movie(id: Long,
              apiKey: String,
              language: String
    ): Observable<Movie> {
        return tmdbApi.movie(id, apiKey, language)
    }

    fun getMovieById(id: Long): Long {
        return id
    }
}