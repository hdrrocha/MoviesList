package com.example.helderrocha.testeparaserinvolvido.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.request.RequestOptions
import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import kotlinx.android.synthetic.main.details_activity.*

private val POSTER_URL = "https://image.tmdb.org/t/p/w154"
private val BACKDROP_URL = "https://image.tmdb.org/t/p/w780"

class MovieImageUrlBuilder {

    fun buildPosterUrl(posterPath: String): String {
        return POSTER_URL + posterPath + "?api_key=" + TmdbApi.API_KEY
    }

    fun buildBackdropUrl(backdropPath: String): String {
        return BACKDROP_URL + backdropPath + "?api_key=" + TmdbApi.API_KEY
    }
}
