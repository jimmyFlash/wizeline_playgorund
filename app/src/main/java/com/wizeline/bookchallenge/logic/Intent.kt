package com.wizeline.bookchallenge.logic

sealed class Intent {
    object LoadAllBooks : Intent()
    object LoadTopRated : Intent()
    data class FilterBooks(val catList: List<String>? = null, val catMatch: Int? = null) : Intent()
}