package com.arctouch.codechallenge.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.example.helderrocha.testeparaserinvolvido.SchedulerProvider
import com.example.helderrocha.testeparaserinvolvido.api.ApiClient
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import com.example.helderrocha.testeparaserinvolvido.data.Cache
import com.example.helderrocha.testeparaserinvolvido.data.DatabaseHelper
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import javax.inject.Inject



class MoviesViewModel @Inject constructor(val api: ApiClient, private val schedulers: SchedulerProvider) : ViewModel() {
    private val moviesData = MoviesLiveData(api, schedulers)
    var dataBase: DatabaseHelper? = null
    fun getData(): LiveData<List<Movie>> = moviesData
    val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun getMoreMovies(page: Long) {
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
                    Cache.cacheMovies(_movies.value!!)
                }, {
                    _movies.value = listOf()
                })
    }
}

class MoviesLiveData(private val api: ApiClient, private val schedulers: SchedulerProvider) : LiveData<List<Movie>>() {

    init {
        loadData()
    }

    private fun loadData() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .doOnSuccess {
                    Cache.cacheGenres(it.genres)
                }
                .flatMapObservable {

                    api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    value = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    Cache.cacheMovies(value!!)
                }, {
                    value = listOf()
                })
    }

}
