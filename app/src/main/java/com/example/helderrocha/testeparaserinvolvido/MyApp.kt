package com.example.helderrocha.testeparaserinvolvido

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class MyApp: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
//        Realm.init(this)
//        val userAddressConfig = RealmConfiguration.Builder().name("movies-db").schemaVersion(1).deleteRealmIfMigrationNeeded().build()
//        // clear previous data for fresh start
//        Realm.deleteRealm(Realm.getDefaultConfiguration())
//        Realm.deleteRealm(userAddressConfig)

        DaggerAppComponent
                .builder()
                .appContext(this)
                .create(this)
                .inject(this)

    }
}