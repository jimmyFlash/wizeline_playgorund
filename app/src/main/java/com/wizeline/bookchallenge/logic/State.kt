package com.wizeline.bookchallenge.logic

import com.wizeline.bookchallenge.BookWRating

sealed class State  {
    data class BooksLoaded(val booksList : List<BookWRating>? )  : State()
    data class FilterBooks(val filterList : List<BookWRating> )  : State()
    data class  TopBooks (val topBook :  List<BookWRating>?)  : State()
    object Loading : State()
    object Idle: State()
    object Error: State()
}