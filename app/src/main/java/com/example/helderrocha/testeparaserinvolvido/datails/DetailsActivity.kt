package com.example.helderrocha.testeparaserinvolvido.datails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arctouch.codechallenge.home.MovieViewModel
import com.arctouch.codechallenge.home.ViewModelFactory


import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
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

        var movieId = data.getLong("movie_selected")

        Toast.makeText(this, "idLong: ${movieId}", Toast.LENGTH_LONG).show()

//        movieViewModel.getMovieById(movieId)
//        movieViewModel.movie.observe(this, movieObserver)


    }

    private fun onMovieFetched(movie: Movie?) {
        Toast.makeText(this, "Clicked: ${movie?.title}", Toast.LENGTH_LONG).show()
    }


}
