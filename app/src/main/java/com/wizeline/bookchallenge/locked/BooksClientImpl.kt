package com.wizeline.bookchallenge.locked

import android.util.Log
import java.text.DecimalFormat
import kotlin.random.Random

class BooksClientImpl: BooksClient {

    override fun getTopRatedBooks(): List<Book> {
        randomDelay()
        val listSize = Random.nextInt(1, Data.bookList.size)
        return Data.bookList.shuffled().take(listSize)
    }

    override fun getRatingForBook(bookId: String): Float {
        randomDelay()
        val decimalFormat = DecimalFormat("#.#")
        val randomRating = Random.nextDouble(4.0,5.0)
        return decimalFormat.format(randomRating).toFloat()
    }

    private fun randomDelay() {
        try {
            Thread.sleep(Random.nextInt(5000).toLong())
        } catch (e: InterruptedException) {
            Log.e(BooksClient::class.java.simpleName, "Thread interrupted")
            Thread.currentThread().interrupt()
        }
    }
}
