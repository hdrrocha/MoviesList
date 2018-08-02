package com.example.helderrocha.testeparaserinvolvido.util

import android.content.Context
import android.net.ConnectivityManager

class ConnectUtil(baseContext: Context) {
    val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo

    fun isConnection(): Boolean {
        return networkInfo != null && networkInfo.isConnected
    }

}