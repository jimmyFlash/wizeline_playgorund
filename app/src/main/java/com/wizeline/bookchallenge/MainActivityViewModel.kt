package com.wizeline.bookchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.bookchallenge.locked.Book
import com.wizeline.bookchallenge.locked.BooksClient
import com.wizeline.bookchallenge.locked.BooksClientImpl
import com.wizeline.bookchallenge.locked.data

class MainActivityViewModel : ViewModel() {

    val topRatedBooksList : MutableLiveData<List<Book>> = MutableLiveData()
    val allBooksList : MutableLiveData<List<Book>> = MutableLiveData()

    private var booksClient: BooksClient = BooksClientImpl()

    init{
        allBooksList.value = data.bookList
       // topRatedBooksList.value = booksClient.getTopRatedBooks()

    }
}