package com.example.helderrocha.testeparaserinvolvido.api


import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module()
class NetworkModule {
    @Provides
    @Singleton
    fun providesTmdbApi(): ApiClient {
        val tmdbApi = Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi::class.java)
        return ApiClient(tmdbApi)
    }
}