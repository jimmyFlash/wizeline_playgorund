package com.wizeline.bookchallenge.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizeline.bookchallenge.MainActivityViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * class that implements the ViewModelProvider.Factory interface (implementation)
 */
class ViewModelFactoryModule @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    /*
    provides implementation of the ViewModelProvider.Factory (ViewModelFactoryModule)
     */
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactoryModule): ViewModelProvider.Factory

    /*
    provides implementation/extension of the ViewModel abstract class (MainActivityViewModel)
     */
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun provideMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel
    //Add more ViewModels here
}