package com.example.helderrocha.testeparaserinvolvido.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import com.example.helderrocha.testeparaserinvolvido.R

import com.example.helderrocha.testeparaserinvolvido.R.*
import com.example.helderrocha.testeparaserinvolvido.datails.DetailsActivity
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var moviesVMFactory: ViewModelFactory<MoviesViewModel>

    private val moviesViewModel by lazy {
        ViewModelProviders.of(this, moviesVMFactory)[MoviesViewModel::class.java]
    }
    private var itensList: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        moviesViewModel.getData().observe(this, Observer(updateList))
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val listScrollLister = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.i("Helder", "layoutManager.itemCount" + layoutManager.itemCount)
                Log.i("Helder", "layoutManager.findLastVisibleItemPosition()" + layoutManager.findLastVisibleItemPosition())
                var currency = layoutManager.findLastVisibleItemPosition()
                if(currency+1 >= itensList){
                    Log.i("Helder", "Posso carregar mais")
                    moviesViewModel.getData().observe(this@HomeActivity, Observer(updateList))
                }
//
            }
        }

        recyclerView.addOnScrollListener(listScrollLister)
    }

    private val updateList: (List<Movie>?) -> Unit = {
        recyclerView.adapter = HomeAdapter((it as ArrayList<Movie>?)!!, { movie : Movie -> partItemClicked(movie) })
        itensList = recyclerView.adapter.itemCount
        Log.i("Helder", "iniciei com:"+ itensList)
        progressBar.visibility = View.GONE
    }

    private fun partItemClicked(movie : Movie) {
        val showDetailActivityIntent = Intent(this, DetailsActivity::class.java)
        showDetailActivityIntent.putExtra("movie_selected", movie.id)
        startActivity(showDetailActivityIntent)
    }
}