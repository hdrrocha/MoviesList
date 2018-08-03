package com.example.helderrocha.testeparaserinvolvido.data

import com.example.helderrocha.testeparaserinvolvido.model.Genre
import hinl.kotlin.database.helper.Database
import hinl.kotlin.database.helper.Schema
import java.util.*


@Database(tableName = "MovieTB")
data class MovieDB(
        @Schema(generatedId = true, field = "id", autoIncrease = true, nonNullable = true) val id: Int? = 0,
        @Schema(field = "title") var title: String?,
        @Schema(field = "overview") var overview: String?,
        @Schema(field = "genres") var genres: String,
        @Schema(field = "poster_path") var poster_path: String?,
        @Schema(field = "backdrop_path") var backdrop_path: String?,
        @Schema(field = "release_date") var release_date: String? = null) {
}
@Database(tableName = "GenreTB")
data class GenreMovie(
        @Schema(field = "id") var id: Int,
        @Schema(field = "name")  var name: String) {}