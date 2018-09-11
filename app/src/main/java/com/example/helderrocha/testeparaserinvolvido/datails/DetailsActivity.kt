package com.example.helderrocha.testeparaserinvolvido.datails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.helderrocha.testeparaserinvolvido.BR


import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.databinding.DetailsActivityBinding
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.util.MovieImageUrlBuilder
import com.example.helderrocha.testeparaserinvolvido.view_model.MovieViewModel
import com.example.helderrocha.testeparaserinvolvido.view_model.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.details_activity.*
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var movieVMFactory: ViewModelFactory<MovieViewModel>

    private val movieViewModel by lazy {
        ViewModelProviders.of(this, movieVMFactory)[MovieViewModel::class.java]
    }


    private val movieObserver = Observer<Movie>(::onMovieFetched)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        AndroidInjection.inject(this)

        val data: Bundle = intent.extras
        var movieId = data.getInt("movie_selected")

        movieViewModel.movie.observe(this, movieObserver)
        movieViewModel.getMovieById(movieId.toLong())

    }

    private fun onMovieFetched(movie: Movie?) {
        val binding: DetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.details_activity)
        val movieImageUrlBuilder = MovieImageUrlBuilder()
        binding.setVariable(BR.movie, movie)
        if (movie != null) {
            genresDetaisTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }

            Glide.with(this)
                    .load(movie.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(imageBackground)

            Glide.with(this)
                    .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(imagCover)
            progressBarDetails.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        finish()
    }
}
