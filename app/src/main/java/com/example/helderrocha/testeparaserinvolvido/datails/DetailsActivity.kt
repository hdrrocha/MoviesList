package com.example.helderrocha.testeparaserinvolvido.datails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.helderrocha.testeparaserinvolvido.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        val data: Bundle = intent.extras
        val movie = data.get("movie_selected")
        Toast.makeText(this, "Clicked: ${movie}", Toast.LENGTH_LONG).show()
    }
}
