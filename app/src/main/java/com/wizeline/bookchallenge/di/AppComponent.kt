package com.wizeline.bookchallenge.di

import android.content.Context
import com.wizeline.bookchallenge.views.HistoryFragment
import com.wizeline.bookchallenge.views.MainActivity
import com.wizeline.bookchallenge.views.MainFragment
import com.wizeline.bookchallenge.views.VectorDrawablesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [ViewModelModule::class, BooksClientModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity : MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(vectorDrawablesFragment: VectorDrawablesFragment)
    fun inject(historyFragment: HistoryFragment)

}