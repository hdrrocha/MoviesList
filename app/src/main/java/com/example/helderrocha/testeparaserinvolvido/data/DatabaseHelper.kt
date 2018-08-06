package com.example.helderrocha.testeparaserinvolvido.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import hinl.kotlin.database.helper.SQLiteDatabaseHelper


class DatabaseHelper(context: Context): SQLiteDatabaseHelper(
        context = context,
        name = "Movies.db",
        factory = null,
        version = 1) {

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}