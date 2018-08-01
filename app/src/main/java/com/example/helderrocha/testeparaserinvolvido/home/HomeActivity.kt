package com.example.helderrocha.testeparaserinvolvido.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.MoviesViewModel
import com.arctouch.codechallenge.home.ViewModelFactory
import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.datails.DetailsActivity
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.util.InfiniteScrollListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var moviesVMFactory: ViewModelFactory<MoviesViewModel>

    private val moviesViewModel by lazy {
        ViewModelProviders.of(this, moviesVMFactory)[MoviesViewModel::class.java]
    }

    private lateinit var adapter: HomeAdapter
    var listMoview: MutableList<Movie> = mutableListOf()
    var layoutManager = LinearLayoutManager(this)
    protected val moviesObserver = Observer<List<Movie>>(::onMoviesFetched)

    

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        moviesViewModel.getData().observe(this, Observer(updateList))
        moviesViewModel.movies.observe(this, moviesObserver)

    }

    private val updateList: (List<Movie>?) -> Unit = {
        setUpdateAdapter((it as ArrayList<Movie>?)!!)

    }

    private fun onMoviesFetched(newMovies: List<Movie>?) {
        if (newMovies != null) {
            setUpdateAdapter(newMovies)
        }

    }
    private fun partItemClicked(movie : Movie) {
        val showDetailActivityIntent = Intent(this, DetailsActivity::class.java)
        showDetailActivityIntent.putExtra("movie_selected", movie.id)
        startActivity(showDetailActivityIntent)
    }

    private  fun setUpdateAdapter(movies: List<Movie>){
        if(listMoview.size == 0){
            listMoview = movies as  MutableList<Movie>
            adapter =  HomeAdapter(listMoview, { movie: Movie -> partItemClicked(movie) } )
            recyclerView.adapter = adapter
            recyclerView.adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE

        } else {
            var currentPosition =(recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            listMoview.addAll(movies!!)
            adapter.notifyDataSetChanged()
            recyclerView.adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE

        }

        recyclerView.addOnScrollListener(InfiniteScrollListener({ moviesViewModel.getMoreMovies() }, layoutManager))
    }
}