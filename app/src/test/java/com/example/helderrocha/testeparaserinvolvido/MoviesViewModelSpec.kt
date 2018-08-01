package com.example.helderrocha.testeparaserinvolvido


import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import com.arctouch.codechallenge.home.MoviesViewModel
import com.example.helderrocha.testeparaserinvolvido.api.ApiClient
import com.example.helderrocha.testeparaserinvolvido.api.TmdbApi
import com.example.helderrocha.testeparaserinvolvido.model.Genre
import com.example.helderrocha.testeparaserinvolvido.model.GenreResponse
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.model.UpcomingMoviesResponse
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito
import org.mockito.Mockito.mock

class MoviesViewModelSpec : FreeSpec({
    class TestSchedulerProvider : SchedulerProvider {
        override fun mainThread(): Scheduler {
            return TestScheduler()
        }

        override fun io(): Scheduler {
            return TestScheduler()
        }
    }

    val tmdbApi = Mockito.mock(TmdbApi::class.java)
    val apiClient = ApiClient(tmdbApi)
    val testScheduler = TestScheduler()
    val testSchedulerProvider = TestSchedulerProvider()

    fun mockApiUpcomingMoviesResponse(response: UpcomingMoviesResponse? = null, error: Error? = null) {
        val mockResponse: Observable<UpcomingMoviesResponse> = if (response != null) Observable.just(response) else Observable.error(error)
        Mockito.`when`(tmdbApi.upcomingMovies(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(mockResponse.subscribeOn(testScheduler).observeOn(testScheduler))
    }

    fun mockApiGenresResponse(response: GenreResponse? = null, error: Error? = null) {
        val mockResponse: Single<GenreResponse> = if (response != null) Single.just(response) else Single.error(error)
        Mockito.`when`(tmdbApi.genres(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockResponse.subscribeOn(testScheduler).observeOn(testScheduler))
    }

    fun <T> observe(result: LiveData<T>, observer: (T?) -> Unit): Unit {
        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        result.observe({ lifecycle }, { observer(it) })
    }

    "getData" - {
        "should return empty list when genres fails" {
            mockApiGenresResponse(error = Error("dummyError"))

            val moviesViewModel = MoviesViewModel(apiClient, testSchedulerProvider)
            val result = moviesViewModel.getData()

            testScheduler.triggerActions()

            observe(result) {
                it shouldBe listOf()
            }
        }

        "genres is successful" - {
            val dummyGenre = Genre(1, "dummyGenre")
            mockApiGenresResponse(response = GenreResponse(listOf(dummyGenre)))

            "should return empty list when upcomingMovies fails" {
                mockApiUpcomingMoviesResponse(error = Error("dummyError"))

                val moviesViewModel = MoviesViewModel(apiClient, testSchedulerProvider)
                val result = moviesViewModel.getData()

                testScheduler.triggerActions()

                observe(result) {
                    it shouldBe listOf()
                }
            }

            "upcomingMovies is successful" - {
                val dummyMovie = Movie(1, "dummyTitle", "dummyOverview", null, listOf(1), null, null, null)
                mockApiUpcomingMoviesResponse(UpcomingMoviesResponse(1, listOf(dummyMovie), 1, 1))

                "should return list of movies" {
                    val moviesViewModel = MoviesViewModel(apiClient, testSchedulerProvider)
                    val result = moviesViewModel.getData()

                    testScheduler.triggerActions()

                    observe(result) {
                        it shouldBe listOf(dummyMovie.copy(genres = listOf(dummyGenre)))
                    }
                }
            }
        }
    }
})