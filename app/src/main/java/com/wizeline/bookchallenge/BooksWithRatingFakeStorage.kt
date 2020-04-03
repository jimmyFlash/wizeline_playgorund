package com.wizeline.bookchallenge

import com.wizeline.bookchallenge.locked.Data
import java.text.DecimalFormat
import java.util.*
import kotlin.random.Random

class BooksWithRatingFakeStorage  {

    companion object{
        private var INSTANCE : BooksWithRatingFakeStorage? = null
        var listsOfBookWrate: List<BookWRating>? = null
        fun createInstance():BooksWithRatingFakeStorage{
            return if(INSTANCE == null){
                BooksWithRatingFakeStorage().also {
                    INSTANCE = it
                    listsOfBookWrate =  createBooksWithRating()
                }
            }else{
                INSTANCE as BooksWithRatingFakeStorage
            }
        }

        private  fun createBooksWithRating(): List<BookWRating>{
            val populateList = mutableListOf<BookWRating>()
            val decimalFormat = DecimalFormat("#.#")
            val randomRating = Random.nextDouble(1.0,5.0)
            Data.bookList.mapTo(populateList, {
                BookWRating(it, decimalFormat.format(randomRating).toFloat())
            })
            return Collections.unmodifiableList(populateList)
        }
    }

     fun getTopRatedBooks(): List<BookWRating>? =
        listsOfBookWrate?.sortedBy {
            it.rating
        }?.filter {
            it.rating > 4.0
        }


    fun getRatingForBook(bookId: String): Float {
        TODO("Not yet implemented")
    }

}