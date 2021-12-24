package com.jimmy.rxandroid.di

import com.jimmy.rxandroid.ui.CheeseFragment
import dagger.Subcomponent

@Subcomponent
interface CheeseComponent{

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): CheeseComponent
    }

    fun inject(cheeseFrag:CheeseFragment)

}