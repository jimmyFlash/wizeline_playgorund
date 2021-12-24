package com.jimmy.rxandroid.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import com.jimmy.rxandroid.R
import com.jimmy.rxandroid.di.CheeseComponent
import com.jimmy.rxandroid.di.CheeseDelegateProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class CheeseFragment : Fragment(), LifecycleObserver {

    private lateinit var cheeseComponentInject: CheeseComponent
    private val compositeDisposable = CompositeDisposable()

    val TAG = CheeseFragment::class.java.simpleName

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var viewModel: CheeseViewModel



    companion object {
        fun newInstance() = CheeseFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cheese_fragment, container, false)
    }

    // bind to the lifecycle OnResume event of the hosting activity
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated(){
        Log.d(TAG, "life cycle listener: onCreated()")
        activity?.lifecycle?.removeObserver(this)// remove lifecycle listener
        
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // attach listener to lifecycle event of the owner activity
        activity?.lifecycle?.addObserver(this)
        cheeseComponentInject = (activity?.applicationContext as CheeseDelegateProvider)
            .providecheeseComponent()
        cheeseComponentInject.inject(this)

        viewModel = ViewModelProvider(viewModelStore, modelFactory)[CheeseViewModel::class.java]
        viewModel.loadCheeseByName("jimmy")
    }


    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }


}