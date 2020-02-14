package com.wizeline.bookchallenge

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wizeline.bookchallenge.locked.Book
import com.wizeline.bookchallenge.locked.data
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var mainActivityViewModel: MainActivityViewModel

    var catList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mainActivityViewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)



        mainActivityViewModel.topRatedBooksList.observe(this, Observer{


            for( b: Book in it){
                Log.e(MainActivity::class.java.simpleName, "Book ${b.id}  ${b.author} ${b.categories.joinToString()} ")
            }

        })

        mainActivityViewModel.allBooksList.observe(this, Observer{


            catList.addAll(data.bookList.map {
                return@map it.categories.joinToString ()
            })

            val filtered = catList.let {
                it.distinct().sortedDescending().reversed()
            }


            catSpinner.adapter = ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_item, filtered)
        })

    }
}
