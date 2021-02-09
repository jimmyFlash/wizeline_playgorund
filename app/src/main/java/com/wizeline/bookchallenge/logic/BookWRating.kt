package com.wizeline.bookchallenge.logic

import com.wizeline.bookchallenge.locked.Book

/**
 * class the combines a book with a random rating
 */
data class BookWRating (val b : Book, val rating : Float)