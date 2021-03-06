package com.example.helderrocha.testeparaserinvolvido.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.helderrocha.testeparaserinvolvido.R
import com.example.helderrocha.testeparaserinvolvido.model.Movie
import com.example.helderrocha.testeparaserinvolvido.util.MovieImageUrlBuilder
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter (var movies: List<Movie>, val clickListener: (Movie) -> Unit) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate
            itemView.setOnClickListener { clickListener(movie)}
            Glide.with(itemView)
                    .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(itemView.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        (holder as ViewHolder).bind(movies[position], clickListener)
    }
}
