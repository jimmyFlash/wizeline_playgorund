package com.wizeline.bookchallenge

import android.app.Application
import com.facebook.stetho.Stetho
import com.jimmy.rxandroid.di.CheeseComponent
import com.jimmy.rxandroid.di.CheeseDelegateProvider
import com.wizeline.bookchallenge.di.AppComponent
import com.wizeline.bookchallenge.di.DaggerAppComponent

open class MyApplication : Application(), CheeseDelegateProvider{

    val appComponent : AppComponent by lazy {

        initializeDagger()
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

    open fun initializeDagger() : AppComponent{
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun providecheeseComponent(): CheeseComponent = appComponent.cheeseComponent().create()
}