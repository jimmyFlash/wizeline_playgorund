package com.wizeline.bookchallenge

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.bookchallenge.adapters.BookRecyclerAdapter
import com.wizeline.bookchallenge.logic.Intent
import com.wizeline.bookchallenge.logic.State
import com.wizeline.bookchallenge.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class MainActivity : AppCompatActivity() {


    // VM reference
    @Inject
    lateinit var mainActivityViewModelFactory: ViewModelProvider.Factory

    lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    // mutable list of rated books holder
    private var filteredBookCatList: MutableList<BookWRating> = mutableListOf()
    // layout manager for the recycler-view display
    private lateinit var linearLayoutManager: LinearLayoutManager
    // displayed book for a certain catagory adapter
    private lateinit var adapter: BookRecyclerAdapter


    // list that hold strings representing the book categories extracted form loaded books
    private var catList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grabs instance of the application graph
        // and populates @Inject fields with objects from the graph
        (application as MyApplication).appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = BuildConfig.APP_NAME

        //
        //mainActivityViewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel =  ViewModelProvider(viewModelStore, mainActivityViewModelFactory)
                                .get(MainActivityViewModel::class.java)

        // set up the recycler view display and divider
        linearLayoutManager = LinearLayoutManager(this)
        binding.bookList.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.bookList.context,
            linearLayoutManager.orientation
        )
        binding.bookList.addItemDecoration(dividerItemDecoration)

        // instansiate the RV adapter
        adapter = BookRecyclerAdapter()

        binding.bookList.adapter = adapter

        // observe the loading state form VM and update ui
        mainActivityViewModel.loading.observe(this, Observer {
            displayProg(it)
        })

        // register item selected listener to the spinner
        binding.catSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                val bookCat = catList[position] // get the selected category string


               when(position){
                   0 ->  mainActivityViewModel.intentChannel.offer(Intent.LoadTopRated)
                   else -> {
                       // call VM method that filters the list of all books and returns on the matching selected
                       // category or a category that contains this category as subset based on the 2nd argument
                       // constant
                       val stringFilerList = convterStringToList(bookCat)
                       mainActivityViewModel.filterBooks(
                           stringFilerList,
                           Constants.EXCAT_CATEGORY_MATCH
                       )
                   }

               }
            }

        }

        if(savedInstanceState == null){
            /*
                The offer() method is a synchronized way to do what the channel’s send() method
                does without launching a new coroutine. As we have control over this channel and know
                it has unlimited capacity, this is a safe way to go and makes things easier for us.
                However, if offer() violates channel’s capacity restrictions or you don’t know them,
                you should use send() instead.
            */

            mainActivityViewModel.intentChannel.offer(Intent.LoadAllBooks)
        }


        mainActivityViewModel.state
            .onEach { state ->  handleState(state)}
            .launchIn(lifecycleScope)

        mainActivityViewModel.catsList.observe(this, Observer {
            catList = it as MutableList<String>
            // populate the spinner with cat. data form filtered cat. titles
            binding.catSpinner.adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, catList)
        })
    }

    private fun handleState(state: State) {
        when(state){
           is  State.Idle -> Unit
           is  State.Error -> handleErrorUi()
           is  State.BooksLoaded -> allBooksSuccess(state.booksList)
           is  State.TopBooks -> filteredBooksSuccess(state.topBook!!)
           is  State.FilterBooks -> filteredBooksSuccess(state.filterList)
        }
    }

    private fun allBooksSuccess(booksList: List<BookWRating>?) {

        // populate the catList with extracted categories
        catList =
            booksList?.map { bookWrating ->
                bookWrating.b.categories.joinToString ()
            }?.distinct()
            ?.sortedDescending()
            ?.reversed() as MutableList<String>

        mainActivityViewModel.updateCategories(catList)

    }

    private fun filteredBooksSuccess(booksList: List<BookWRating>) {
        if(booksList.isNotEmpty()) {
            filteredBookCatList.clear()
            filteredBookCatList.addAll(booksList)
            adapter.swap(filteredBookCatList)
        }
    }

    private fun handleErrorUi() {
        TODO("Not yet implemented")
    }

    /**
     * helper method that converts a string to list
     * can be converted to extension function!
     * @param [s] the string to convert to List<String>
     * @return List of strings representing the categories for certain book
     */
    fun  convterStringToList (s : String): List<String> {
        return s.split(",").map { it.trim() }
    }

    /**
     * utility method that displays/hides the loading spinner and and recyclerView to emulate data
     *  being loaded
     *  @param [loading] flag to indicate whether it's loading(true) or not (false)
     */
    fun displayProg(loading : Boolean){

        if(loading){
            binding.progres.visibility = View.VISIBLE
            binding.bookList.visibility = View.INVISIBLE
        }else{
            binding.progres.visibility = View.GONE
            binding.bookList.visibility = View.VISIBLE
        }
    }

}
