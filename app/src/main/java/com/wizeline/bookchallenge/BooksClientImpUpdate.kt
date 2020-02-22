package com.wizeline.bookchallenge

import android.util.Log
import com.wizeline.bookchallenge.locked.Book
import com.wizeline.bookchallenge.locked.BooksClient
import com.wizeline.bookchallenge.locked.data
import java.text.DecimalFormat
import kotlin.random.Random

// todo create list with presisatnt book ratings
// todo centralize the dependencies
class BooksClientImpUpdate : BooksClient {

    override fun getTopRatedBooks(): List<Book> {
        val listSize = Random.nextInt(1, data.bookList.size)
        return data.bookList.shuffled().take(listSize)
    }

    override fun getRatingForBook(bookId: String): Float {

        val decimalFormat = DecimalFormat("#.#")
        val randomRating = Random.nextDouble(1.0,5.0)
        return decimalFormat.format(randomRating).toFloat()
    }

     fun getTopRatedBooksWDelay(delayAmnt: Long): List<Book> {
         randomDelay(delayAmnt)
         return getTopRatedBooks()
     }

     fun getRatingForBookWDelay(bookId: String, delayAmnt: Long): Float {
         randomDelay(delayAmnt)
         return getRatingForBook(bookId)
     }

    private fun randomDelay(delayAmnt : Long) {
        try {
            Thread.sleep(delayAmnt)
        } catch (e: InterruptedException) {
            Log.e(BooksClientImpUpdate::class.java.simpleName, "Thread interrupted")
            Thread.currentThread().interrupt()
        }
    }

}