package com.arctouch.codechallenge.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.view.View
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import com.example.helderrocha.testeparaserinvolvido.data.Cache
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class HomeViewModel: ViewModel() {
    var moviesData =  MutableLiveData<List<Movie>>().apply { emptyArray<Movie>() }
    var  arraylistmutablelivedata= MutableLiveData<ArrayList<Movie>>()

    var arraylist = ArrayList<Movie>()

    protected val api: TmdbApi = Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    init {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    loadMovies()
                }
    }

    fun loadMovies() {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    arraylist.addAll(moviesWithGenres)

                }
    }


    fun getArrayList(): ArrayList<Movie>
    {
//        arraylistmutablelivedata.value = arraylist
//        arraylistmutablelivedata.value = arraylist

        return arraylist
    }

}