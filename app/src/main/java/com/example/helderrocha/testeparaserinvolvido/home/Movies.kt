package com.arctouch.codechallenge.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.helderrocha.testeparaserinvolvido.SchedulerProvider
import com.example.helderrocha.testeparaserinvolvido.api.ApiClient
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import com.example.helderrocha.testeparaserinvolvido.data.Cache
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import dagger.Lazy
import javax.inject.Inject

class ViewModelFactory<VM : ViewModel> @Inject constructor(private val viewModel: Lazy<VM>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel.get() as T
    }
}

class MoviesViewModel @Inject constructor(api: ApiClient, private val schedulers: SchedulerProvider) : ViewModel() {
    private val moviesData = MoviesLiveData(api, schedulers)

    fun getData(): LiveData<List<Movie>> = moviesData
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
                    api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    value = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                }, {
                    value = listOf()
                })
    }
}