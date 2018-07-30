package com.example.helderrocha.testeparaserinvolvido.datails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.helderrocha.testeparaserinvolvido.SchedulerProvider
import com.example.helderrocha.testeparaserinvolvido.api.ApiClient
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import com.example.helderrocha.testeparaserinvolvido.data.Cache
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import dagger.Lazy
import javax.inject.Inject


//class ViewModelFactory<VM : ViewModel> @Inject constructor(private val viewModel: Lazy<VM>) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return viewModel.get() as T
//    }
//}
////class CustomViewModelFactory(private val test: String) : ViewModelProvider.NewInstanceFactory() {
////
////    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
////        return MovieViewModel(test) as T
////    }
//
////}
//class MovieViewModel @Inject constructor(api: ApiClient, private val schedulers: SchedulerProvider) : ViewModel() {
//    val id = MutableLiveData<Long>()
//    val idMovie: Long = id.value!!
//    private val movieData = MovieLiveData(idMovie,api, schedulers)
//
//    fun getData(): LiveData<Movie> = movieData
//}
//
//class MovieLiveData(private val id: Long, private val api: ApiClient, private val schedulers: SchedulerProvider) : LiveData<Movie>() {
//    init {
//        loadData()
//    }
//    private fun loadData() {
//        api.movie(id, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
//                .subscribeOn(schedulers.io())
//                .observeOn(schedulers.mainThread())
//                .subscribe({value})
//    }
//}