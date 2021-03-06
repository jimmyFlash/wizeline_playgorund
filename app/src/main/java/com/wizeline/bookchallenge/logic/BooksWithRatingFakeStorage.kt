package com.wizeline.bookchallenge.logic

import com.wizeline.bookchallenge.locked.Data
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class BooksWithRatingFakeStorage  @Inject constructor(){

    companion object{
        private var INSTANCE : BooksWithRatingFakeStorage? = null
        var listsOfBookWrate: List<BookWRating>? = null
      /*  fun createInstance():BooksWithRatingFakeStorage{
            return if(INSTANCE == null){
                BooksWithRatingFakeStorage().also {
                    INSTANCE = it
                    listsOfBookWrate =  createBooksWithRating()
                }
            }else{
                INSTANCE as BooksWithRatingFakeStorage
            }
        }*/

        init {
            listsOfBookWrate =  createBooksWithRating()
        }

        private  fun createBooksWithRating(): List<BookWRating>{
            val populateList = mutableListOf<BookWRating>()
            val decimalFormat = DecimalFormat("#.#")

            Data.bookList.mapTo(populateList, {
                val randomRating = Random.nextDouble(1.0,4.0) + 1.0
                BookWRating(it, decimalFormat.format(randomRating).toFloat())
            })
            return Collections.unmodifiableList(populateList)
        }
    }

     fun getTopRatedBooks(): List<BookWRating>? =
        listsOfBookWrate?.sortedByDescending {
            it.rating
        }?.filter {
            it.rating > 4.0
        }

    fun getAllBooks() = listsOfBookWrate


    fun getRatingForBook(bookId: String): Float? {
        return listsOfBookWrate?.filter { it.b.id == bookId }
            ?.elementAtOrNull(0)?.rating
    }

}