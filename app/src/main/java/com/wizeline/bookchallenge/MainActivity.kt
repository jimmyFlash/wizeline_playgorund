package com.wizeline.bookchallenge

import android.os.Bundle
import android.os.Handler
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
import com.wizeline.bookchallenge.locked.data
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private lateinit var filtered: List<String>
    private var allBok: List<Book> = listOf()
    lateinit var mainActivityViewModel: MainActivityViewModel

    private var filteredBookCatList: MutableList<BookWRating> = mutableListOf()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: BookRecyclerAdapter


    private var catList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)

        linearLayoutManager = LinearLayoutManager(this)
        bookList.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            bookList.getContext(),
            linearLayoutManager.getOrientation()
        )
        bookList.addItemDecoration(dividerItemDecoration)

        adapter = BookRecyclerAdapter()

        bookList.adapter = adapter

        mainActivityViewModel.loading.observe(this, Observer {
            displayProg(it)
        })
        mainActivityViewModel.filteredBooksList.observe(this, Observer{
            if(it.isNotEmpty()) {
                filteredBookCatList.clear()
                filteredBookCatList.addAll(it)
                adapter.swap(filteredBookCatList)
            }
        })

        mainActivityViewModel.allBooksList.observe(this, Observer{

            allBok = it

            catList.addAll(data.bookList.map { book ->
                return@map book.categories.joinToString ()
            })

             filtered = catList.let {
                it.distinct().sortedDescending().reversed()
            }
            catSpinner.adapter = ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_item, filtered)
        })

        catSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                displayProg(true)
                val bookCat = filtered[position]

                        mainActivityViewModel.filterBooks( convterStringToList (bookCat),
                            Constants.EXCAT_CATEGORY_MATCH)
            }

        }
    }

    fun  convterStringToList (s : String): List<String> {
        return s.split(",").map { it.trim() }
    }

    fun displayProg(boll : Boolean){

        if(boll){
            progres.visibility = View.VISIBLE
            bookList.visibility = View.INVISIBLE
        }else{
            progres.visibility = View.GONE
            bookList.visibility = View.VISIBLE
        }
    }

}
