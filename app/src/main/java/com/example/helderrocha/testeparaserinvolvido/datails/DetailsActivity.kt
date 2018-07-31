package com.example.helderrocha.testeparaserinvolvido.datails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.MovieViewModel
import com.arctouch.codechallenge.home.ViewModelFactory
import com.example.helderrocha.testeparaserinvolvido.BR


import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.databinding.DetailsActivityBinding
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var movieVMFactory: ViewModelFactory<MovieViewModel>

    private  val  movieViewModel by lazy {
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
        Toast.makeText(this, "Movie: ${movie?.title}", Toast.LENGTH_LONG).show()

        val binding: DetailsActivityBinding =  DataBindingUtil.setContentView(this, R.layout.details_activity)

        val _movie = movie
        binding.setVariable(BR.movie, movie)
        binding.executePendingBindings()
    }

}
