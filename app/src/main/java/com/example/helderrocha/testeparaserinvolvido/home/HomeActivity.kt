package com.example.helderrocha.testeparaserinvolvido.home_c

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.home.MoviesViewModel
import com.arctouch.codechallenge.home.ViewModelFactory
import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.data.Cache
import com.example.helderrocha.testeparaserinvolvido.data.DatabaseHelper
import com.example.helderrocha.testeparaserinvolvido.data.MovieDB
import com.example.helderrocha.testeparaserinvolvido.datails.DetailsActivity
import com.example.helderrocha.testeparaserinvolvido.home.adapter.MovieAdapter
import com.example.helderrocha.testeparaserinvolvido.home.adapter.MovieAdapterOff
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.util.ConnectUtil
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.home_activity.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var moviesVMFactory: ViewModelFactory<MoviesViewModel>

    private val moviesViewModel by lazy {
        ViewModelProviders.of(this, moviesVMFactory)[MoviesViewModel::class.java]
    }

    private lateinit var adapter: MovieAdapter
    private lateinit var adapterOff: MovieAdapterOff
    var listMoview: MutableList<Movie> = mutableListOf()
    var layoutManager = LinearLayoutManager(this)
    protected val moviesObserver = Observer<List<Movie>>(::onMoviesFetched)

    var totalItemCount: Int = 0
    var visibleItemCount: Int = 0
    var pastVisibleItemCount: Int = 0
    var loading: Boolean = false
    var page: Long = 1L
    var dataBase: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        dbConfig()
        recoverComponents()
    }

    private fun recoverComponents() {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        setEventComponents()
    }

    private fun setEventComponents() {
        val connection = ConnectUtil(this.baseContext)
        if(connection.isConnection()){
            moviesViewModel.getData().observe(this, Observer(updateList))
            moviesViewModel.movies.observe(this, moviesObserver)
        } else {
            setUpdateAdapter(Cache.movies, false)
        }
    }

    private fun dbConfig() {
        dataBase = DatabaseHelper(this@HomeActivity)
        createTable(dataBase!!)
    }

    private val updateList: (List<Movie>?) -> Unit = {
        loading = true
        val  listMovieLocal: List<Movie>
        listMovieLocal = (it as ArrayList<Movie>)
        setUpdateAdapter(listMovieLocal, true)
    }

    private fun onMoviesFetched(newMovies: List<Movie>?) {
        if (newMovies != null) {
            loading = true
            setUpdateAdapter(newMovies, true)
        } else {
            Toast.makeText(this, "There is no new movie", Toast.LENGTH_SHORT).show()
        }

    }
    private fun partItemClicked(movie : Movie) {
        val showDetailActivityIntent = Intent(this, DetailsActivity::class.java)
        showDetailActivityIntent.putExtra("movie_selected", movie.id)
        startActivity(showDetailActivityIntent)
    }

    private fun setUpdateAdapter(movies: List<Movie>, isConnected: Boolean){
        if(isConnected){

            movies.forEach { movie ->
                insertContent(this.dataBase!!, movie)
            }

            if(listMoview.size == 0){
                listMoview = movies as MutableList<Movie>
                adapter = MovieAdapter(listMoview, { movie: Movie -> partItemClicked(movie) } )
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
        } else {
            val listOfExample = readContent(this.dataBase!!)
            listMoview = (listOfExample as List<Movie>).toMutableList()
            adapterOff = MovieAdapterOff(listOfExample, { movie: Movie -> partItemClicked(movie) } )
            recyclerView.adapter = adapterOff
            recyclerView.adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            Toast.makeText(this, "You must be connected to the internet to have full access to the movie list", Toast.LENGTH_SHORT).show()
        }

    }



    fun createTable(database: DatabaseHelper) {
        database.createTable(MovieDB::class)
    }

    fun insertContent(database: DatabaseHelper, movie: Movie) {
        val genres = StringBuilder()
        movie.genres!!.forEach { genre ->
           genres.append(genre.name+", ")
        }

        var stringG: String
        stringG=  genres.toString()
        val movie = MovieDB(title = movie.title, overview = movie.overview,genres = stringG, poster_path = movie.posterPath,backdrop_path = movie.backdropPath, release_date = movie.releaseDate)
        database.insert(movie)
    }

    fun readContent(database: DatabaseHelper): List<MovieDB>? = database.get(MovieDB::class)

}