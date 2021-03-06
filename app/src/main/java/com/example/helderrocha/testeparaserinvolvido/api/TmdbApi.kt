package com.example.helderrocha.testeparaserinvolvido.api

import com.example.helderrocha.testeparaserinvolvido.model.GenreResponse
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.model.UpcomingMoviesResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    companion object {
        const val URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "1f54bd990f1cdfb230adb312546d765d"
        //get inifinite movies
        const val DEFAULT_LANGUAGE = ""
        const val DEFAULT_REGION = ""

//        const val DEFAULT_LANGUAGE = "en-US"
//        const val DEFAULT_REGION = "US"
    }

    @GET("genre/movie/list")
    fun genres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Single<GenreResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Long,
            @Query("region") region: String
    ): Observable<UpcomingMoviesResponse>

    @GET("movie/{id}")
    fun movie(
            @Path("id") id: Long,
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Observable<Movie>
}
