package com.arctouch.codechallenge.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.example.helderrocha.testeparaserinvolvido.SchedulerProvider
import com.example.helderrocha.testeparaserinvolvido.api.ApiClient
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import com.example.helderrocha.testeparaserinvolvido.data.Cache
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ViewModelFactory<VM : ViewModel> @Inject constructor(private val viewModel: Lazy<VM>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel.get() as T
    }
}

class MoviesViewModel @Inject constructor(val api: ApiClient, private val schedulers: SchedulerProvider) : ViewModel() {
    private val moviesData = MoviesLiveData(api, schedulers)

    fun getData(): LiveData<List<Movie>> = moviesData
    var page = 1L

    val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun getMoreMovies() {
        page++
        Log.i("Helder", "ViewModel page" + page)
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .doOnSuccess {
                    Cache.cacheGenres(it.genres)
                }
                .flatMapObservable {

                    api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    _movies.value = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                }, {
                    _movies.value = listOf()
                })
    }


}

class MoviesLiveData(private val api: ApiClient, private val schedulers: SchedulerProvider) : LiveData<List<Movie>>() {
    var page = 1L

    init {
        loadData()
    }

    private fun loadData() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .doOnSuccess {
                    Cache.cacheGenres(it.genres)
                }
                .flatMapObservable {

                    api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    value = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                }, {
                    value = listOf()
                    Cache.cacheMovies(value!!)
                })
    }
}

class MovieViewModel @Inject constructor( val api: ApiClient, private val schedulers: SchedulerProvider) : ViewModel() {

    val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    fun getMovieById(id: Long) {
        api.movie(id, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _movie.value = it}, { /* error */ })
    }
}