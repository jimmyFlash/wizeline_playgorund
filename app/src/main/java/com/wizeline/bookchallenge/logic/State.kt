package com.wizeline.bookchallenge.logic

import com.wizeline.bookchallenge.BookWRating

sealed class State  {
    data class BooksLoaded(val booksList : List<BookWRating>? )  : State()
    object Loading : State()
    object Idel: State()
    object Error: State()
}