package com.wizeline.bookchallenge

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.bookchallenge.adapters.BookRecyclerAdapter
import com.wizeline.bookchallenge.locked.Book
import com.wizeline.bookchallenge.locked.Data
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {


    // list of book unique book categories sorted in desc. order
    private lateinit var filtered: List<String>


    // VM reference
    @Inject
    lateinit var mainActivityViewModelFactory: ViewModelProvider.Factory

    lateinit var mainActivityViewModel: MainActivityViewModel

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

        setContentView(R.layout.activity_main)

        //
        //mainActivityViewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel =  ViewModelProvider(viewModelStore, mainActivityViewModelFactory)
                                .get(MainActivityViewModel::class.java)

        // set up the recycler view display and divider
        linearLayoutManager = LinearLayoutManager(this)
        bookList.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            bookList.context,
            linearLayoutManager.getOrientation()
        )
        bookList.addItemDecoration(dividerItemDecoration)

        // instansiate the RV adapter
        adapter = BookRecyclerAdapter()

        bookList.adapter = adapter

        // observe the loading state form VM and update ui
        mainActivityViewModel.loading.observe(this, Observer {
            displayProg(it)
        })

        // observer the list of loaded books per category and update RV adapter
        mainActivityViewModel.filteredBooksList.observe(this, Observer{
            if(it.isNotEmpty()) {
                filteredBookCatList.clear()
                filteredBookCatList.addAll(it)
                adapter.swap(filteredBookCatList)
            }
        })

        // observer the full list of books loaded
        mainActivityViewModel.allBooksList.observe(this, Observer{

            // populate the catList with extracted categories
            catList.addAll(it.map { book ->
                return@map book.categories.joinToString ()
            })

            // create a list filtered and sorted unique categories
             filtered = catList.distinct().sortedDescending().reversed()

            // populate the spinner with cat. data form filtered cat. titles
            catSpinner.adapter = ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_item, filtered)
        })

        // register item selected listener to the spinner
        catSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                displayProg(true) // display progress bar
                val bookCat = filtered[position] // get the selected category string

                // call VM method that filters the list of all books and returns on the matching selected
                // category or a category that contains this category as subset based on the 2nd argument
                // constant
                mainActivityViewModel.filterBooks( convterStringToList (bookCat),
                    Constants.EXCAT_CATEGORY_MATCH)
            }

        }
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
            progres.visibility = View.VISIBLE
            bookList.visibility = View.INVISIBLE
        }else{
            progres.visibility = View.GONE
            bookList.visibility = View.VISIBLE
        }
    }

}
