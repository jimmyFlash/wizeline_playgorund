package com.wizeline.bookchallenge

import com.wizeline.bookchallenge.locked.Book

/**
 * class the combines a book with a random rating
 */
data class BookWRating (val b : Book, val rating : Float)