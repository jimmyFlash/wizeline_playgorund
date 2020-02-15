package com.wizeline.bookchallenge

/**
 * object with flags used to determine if you want to display list of books with category
 * that exactly matches that of books found in the list of all books
 * example : only listOf("Childrens", "Fiction")
 * or
 * parent categories that could also contain the categories or the selected books within them as subset
 * example : listOf("Childrens", "Fiction") also get those with ("Childrens", "Fiction", "Humor", "Picture books").
 * since they also have the selected criteria as part of them
 *
 */
object Constants {
    const val EXCAT_CATEGORY_MATCH = 1
    const val PARTIAL_CATAGORY_MATCH = 2
}