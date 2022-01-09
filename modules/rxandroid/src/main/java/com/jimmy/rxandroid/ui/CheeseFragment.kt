package com.jimmy.rxandroid.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jimmy.rxandroid.R
import com.jimmy.rxandroid.di.CheeseComponent
import com.jimmy.rxandroid.di.CheeseDelegateProvider
import com.jimmy.rxandroid.storage.Cheese
import com.jimmy.rxandroid.storage.CheeseDatabase
import com.jimmy.rxandroid.ui.adapters.CheeseAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CheeseFragment : Fragment(), LifecycleObserver {

    // fragment is injected through a CheeseComponent sub component
    private lateinit var cheeseComponentInject: CheeseComponent

    private val compositeDisposable = CompositeDisposable()

    private val cheeseAdapter = CheeseAdapter()

    // inject the viewmodel factory from ViewModelModule
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    // viewmodel will be injected through ViewModelModule in app module
    lateinit var viewModel: CheeseViewModel

    // ui elements
    private lateinit var progressBar :ProgressBar
    private lateinit var searchButton : Button
    private lateinit var queryEditText : EditText

    private lateinit var disposable: Disposable


    companion object {
        fun newInstance() = CheeseFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        val view = inflater.inflate(R.layout.cheese_fragment, container, false)

        view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter =cheeseAdapter
        }

        progressBar = view.findViewById(R.id.progressBar)
        searchButton= view.findViewById(R.id.searchButton)
        queryEditText= view.findViewById(R.id.queryEditText)

        // create an observable
        val buttonClickStream  = createButtonClickObservable()
            // Convert the button click stream into a flowable using LATEST BackpressureStrategy.
            .toFlowable(BackpressureStrategy.LATEST)

        val textChangeStream = createTextChangeObservable()
            // Convert the text input change stream into a flowable using BUFFER BackpressureStrategy
            .toFlowable(BackpressureStrategy.BUFFER)

        /*
            implementing observable that reacted to button clicks and text field changes.
            merge: takes items from two or more observables and puts them into a single observable
         */
        val searchTextFlowable = Flowable.merge<String>(buttonClickStream, textChangeStream)

        disposable = searchTextFlowable

            // next operator in chain will be run on the main thread.
            .observeOn(AndroidSchedulers.mainThread())
            //  showProgress() will be called every time a new item is emitted.
            .doOnNext { showProgress() }
            // next operator should be called on the I/O thread
            .observeOn(Schedulers.io())
            // For each search query, you return a list of results.
            .map {viewModel.searchCheeseName(it) }
            // apply simulated delay
            .delay(2000, TimeUnit.MILLISECONDS)
            // make sure that the results are passed to the list on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            // Subscribe to the observable with subscribe(), and supply a simple Consumer (query)
            .subscribe { query ->
                // hideProgress() when you are just about to display a result.
                hideProgress()
                // perform the search and show the results
                showResult(query)
            }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // since the fragment in this module doesn't know about the application
        // it will delegate the call to the implementation of CheeseDelegateProvider interface
        // which at runtime will be the main application defined in app module
        // to create the CheeseComponent
        cheeseComponentInject = (activity?.applicationContext as CheeseDelegateProvider)
            .providecheeseComponent()
        // inject the fragment from CheeseComponent instance
        cheeseComponentInject.inject(this)

        //  inject viewModel
        viewModel = ViewModelProvider(viewModelStore, modelFactory)[CheeseViewModel::class.java]
//        viewModel.loadCheeseByName("jimmy")

        // create and populate room Db with predefined cheeses
        // subscribe to the flowable list
        val initialLoadDisposable = viewModel.loadInitData().subscribe()
        // add to composite disposable to destroy when life cycle end
        compositeDisposable.add(initialLoadDisposable)

        // observer dummy data insert state into db from viewModel
        viewModel.dataInserted.observe(this@CheeseFragment){dataInsert ->

            if(dataInsert) {
                Toast.makeText(context, context.getString(R.string.success),
                    Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, context.getString(R.string.error_inserting_cheeses),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    // show a progress bar
    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    // hide a progress bar
    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    /**
     * update adapter to display a list of cheeses, or taost if none found
     * @param result of cheeses
     */
    private fun showResult(result: List<Cheese>) {
        if (result.isEmpty()) {
            Toast.makeText(context, R.string.nothing_found, Toast.LENGTH_SHORT).show()
        }
        // update recycler adapter with new items
        cheeseAdapter.submitList(result)
    }

    override fun onDetach() {
        super.onDetach()
        // remove the instance reference to the room Db
         CheeseDatabase.destroyInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        // remove composite disposable
        compositeDisposable.clear()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }


    /**
     * function that returns an observable that will emit strings
     */
    private fun createButtonClickObservable(): Observable<String> {

        // create an observable with Observable.create(),
        // and supply it with a new ObservableOnSubscribe. (emitter)
        return Observable.create { emitter ->
            // Set up an OnClickListener on searchButton.
            searchButton.setOnClickListener {

                // click event happens, call onNext on the emitter
                // and pass it the current text value of queryEditText.
                emitter.onNext(queryEditText.text.toString())
            }

            // ObservableEmitter has setCancellable(). Override cancel(),
            // and your implementation will be called when the Observable is disposed,
            // such as when the Observable is completed or all Observers have unsubscribed from it
            emitter.setCancellable {
                // For OnClickListener, the code that removes the listener is setOnClickListener(null).
                searchButton.setOnClickListener(null)
            }
        }
    }

    /**
     * function that will return an observable for text changes.
     * emitting strings
     */
    private fun createTextChangeObservable(): Observable<String> {

        // Create textChangeObservable observable with create(), which takes an ObservableOnSubscribe
        val textChangeObservable = Observable.create<String> { emitter ->
            // When an observer makes a subscription, the first thing
            // to do is to create a TextWatcher
            val textWatcher = object : TextWatcher {

                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(s: CharSequence?, start: Int,
                                               count: Int, after: Int) = Unit

                // When the user types and onTextChanged() triggers,
                // pass the new text value to an observer.
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }
            }

            // Add the watcher to your TextView by calling addTextChangedListener().
            queryEditText.addTextChangedListener(textWatcher)

            // remove your watcher. To do this, call emitter.setCancellable() and
            // overwrite cancel() to call removeTextChangedListener()
            emitter.setCancellable {
                queryEditText.removeTextChangedListener(textWatcher)
            }
        }
        return textChangeObservable
            // text queries with length less than 2 won’t get sent down the chain
            .filter { it.length > 2 }
            // don’t want to send a new request to the server every time the query
            // is changed by one symbol. debounce waits for a specified amount of time after each
            // item emission for another item. If no item happens to be emitted during this wait,
            // the last item is finally emitted
            .debounce(1000, TimeUnit.MILLISECONDS)
    }
}