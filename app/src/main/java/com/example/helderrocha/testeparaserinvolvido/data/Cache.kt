package com.example.helderrocha.testeparaserinvolvido.data

import com.example.helderrocha.testeparaserinvolvido.model.Genre


object Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }
}
