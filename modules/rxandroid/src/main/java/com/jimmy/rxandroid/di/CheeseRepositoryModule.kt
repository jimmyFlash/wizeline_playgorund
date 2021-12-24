package com.jimmy.rxandroid.di

import com.jimmy.rxandroid.data.CheeseRepository
import dagger.Binds
import dagger.Module

@Module
abstract class  CheeseRepositoryModule {

    @Binds
    abstract fun provideCheeseRepository(cheeseRepo:CheeseRepository):CheeseRepository
}