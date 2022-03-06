package com.wizeline.bookchallenge.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jimmy.rxandroid.ui.CheeseViewModel
import com.wizeline.bookchallenge.views.MainActivityViewModel
import com.wizeline.bookchallenge.views.MainFragmentViewModel
import com.wizeline.bookchallenge.views.VectorDrawablesViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * class that implements the ViewModelProvider.Factory interface (implementation)
 * ViewModelFactory that will provide us a right ViewModel from ViewModelModule
 */
class ViewModelFactoryModule @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

// ViewModelKey is an annotation for using as a key in the Map
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

/**
 * ViewModelModule is responsible for binding all over ViewModel classes into
 * ViewModelFactoryModule constructor arguments
 */
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

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    internal abstract fun provideMainFragmentViewModel(viewModel: MainFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VectorDrawablesViewModel::class)
    internal abstract fun provideVectorDrawablesViewModel(viewModel: VectorDrawablesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheeseViewModel::class)
    internal abstract fun provideCheeseViewModel(viewModel:CheeseViewModel): ViewModel

}