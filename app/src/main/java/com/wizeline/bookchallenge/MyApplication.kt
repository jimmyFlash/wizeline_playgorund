package com.wizeline.bookchallenge

import android.app.Application
import com.wizeline.bookchallenge.di.AppComponent
import com.wizeline.bookchallenge.di.DaggerAppComponent

class MyApplication : Application(){

    val appComponent : AppComponent by lazy {

        initializeDagger()
    }

    open fun initializeDagger() : AppComponent{
        return DaggerAppComponent.factory().create(applicationContext)
    }
}