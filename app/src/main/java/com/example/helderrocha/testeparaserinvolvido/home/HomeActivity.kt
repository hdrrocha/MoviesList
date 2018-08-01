package com.example.helderrocha.testeparaserinvolvido.home_c

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
import com.arctouch.codechallenge.home.MoviesViewModel
import com.arctouch.codechallenge.home.ViewModelFactory
import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.datails.DetailsActivity
import com.example.helderrocha.testeparaserinvolvido.home.adapter.MovieAdapter
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

    private lateinit var adapter: MovieAdapter
    var listMoview: MutableList<Movie> = mutableListOf()
    var layoutManager = LinearLayoutManager(this)
    protected val moviesObserver = Observer<List<Movie>>(::onMoviesFetched)

    var totalItemCount: Int = 0
    var visibleItemCount: Int = 0
    var pastVisibleItemCount: Int = 0
    var loading: Boolean = false
    var page: Long = 1L


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
        loading = true
        setUpdateAdapter((it as ArrayList<Movie>?)!!)

    }

    private fun onMoviesFetched(newMovies: List<Movie>?) {
        if (newMovies != null) {
            loading = true
            setUpdateAdapter(newMovies)
        } else {
            Toast.makeText(this, "There is no new movie", Toast.LENGTH_SHORT).show()
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
            adapter =  MovieAdapter(listMoview, { movie: Movie -> partItemClicked(movie) } )
            recyclerView.adapter = adapter
            recyclerView.adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE

        } else {
            var currentPosition =(recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            listMoview.addAll(movies!!)
            adapter.notifyDataSetChanged()
            recyclerView.adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(currentPosition)
            progressBar.visibility = View.GONE

        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if(dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItemCount =(recyclerView!!.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    Log.i("Helder", "Soma" + (visibleItemCount+ pastVisibleItemCount).toString() )
                    Log.i("Helder", "Soma" + totalItemCount.toString() )
                    if(loading){
                        if((visibleItemCount+ pastVisibleItemCount) >= totalItemCount) {
                            progressBar.visibility = View.VISIBLE
                            loading = false
                            page++
                            moviesViewModel.getMoreMovies(page++)
                        }
                    }
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
}