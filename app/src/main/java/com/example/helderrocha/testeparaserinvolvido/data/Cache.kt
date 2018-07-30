package com.example.helderrocha.testeparaserinvolvido.data

import com.example.helderrocha.testeparaserinvolvido.model.Genre
import com.example.helderrocha.testeparaserinvolvido.model.Movie


object Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }

    var movies = listOf<Movie>()

    fun cacheMovies(genres: List<Movie>) {
        this.movies = movies
    }

}
