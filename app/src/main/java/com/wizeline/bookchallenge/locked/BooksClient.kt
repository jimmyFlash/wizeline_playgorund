package com.wizeline.bookchallenge.locked

interface BooksClient {

    fun getTopRatedBooks(): List<Book>

    fun getRatingForBook(bookId: String): Float

}
