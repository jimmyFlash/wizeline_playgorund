package com.jimmy.rxandroid.di

/**
 * this interface implemented in  application to invoke the CheeseComponent sub component
 * from this module
 */
interface CheeseDelegateProvider {
    fun providecheeseComponent():CheeseComponent
}