package com.example.helderrocha.testeparaserinvolvido.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.MoviesViewModel
import com.arctouch.codechallenge.home.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.helderrocha.testeparaserinvolvido.BR
import com.example.helderrocha.testeparaserinvolvido.R

import com.example.helderrocha.testeparaserinvolvido.R.*
import com.example.helderrocha.testeparaserinvolvido.databinding.DetailsActivityBinding
import com.example.helderrocha.testeparaserinvolvido.datails.DetailsActivity
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.util.MovieImageUrlBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.details_activity.*
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var moviesVMFactory: ViewModelFactory<MoviesViewModel>

    private val moviesViewModel by lazy {
        ViewModelProviders.of(this, moviesVMFactory)[MoviesViewModel::class.java]
    }

    private var itensList: Int = 0
    private lateinit var adapter: HomeAdapter
    protected var listMovies: ArrayList<Movie> = ArrayList()

    protected val moviesObserver = Observer<List<Movie>>(::onMoviesFetched)
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        moviesViewModel.getData().observe(this, Observer(updateList))
        moviesViewModel.movies.observe(this, moviesObserver)

        adapter =  HomeAdapter(listMovies, { movie: Movie -> partItemClicked(movie) })
        val listScrollLister = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var currency = layoutManager.findLastVisibleItemPosition() + 1
                if(currency >= itensList){
                    Log.i("Helder", "Posso carregar mais")
                    Log.i("Helder", "itensList" + itensList)
                    Log.i("Helder", "currency" + currency)
                    moviesViewModel.getMoreMovies()
//                    moviesViewModel.getData().observe(this@HomeActivity, Observer(updateList))

                }
            }
        }

        recyclerView.addOnScrollListener(listScrollLister)
    }

    private val updateList: (List<Movie>?) -> Unit = {
        listMovies.addAll((it as ArrayList<Movie>?)!!)
        adapter.addAll(listMovies)
        recyclerView.adapter = adapter
        itensList = recyclerView.adapter.itemCount
        progressBar.visibility = View.GONE
    }

    private fun onMoviesFetched(newMovies: List<Movie>?) {
        if (newMovies != null) {
            listMovies.addAll(newMovies)
            adapter.addAll(listMovies)
            adapter.notifyDataSetChanged()
        }

    }
    private fun partItemClicked(movie : Movie) {
        val showDetailActivityIntent = Intent(this, DetailsActivity::class.java)
        showDetailActivityIntent.putExtra("movie_selected", movie.id)
        startActivity(showDetailActivityIntent)
    }
}