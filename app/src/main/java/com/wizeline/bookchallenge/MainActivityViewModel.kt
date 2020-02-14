package com.wizeline.bookchallenge

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.bookchallenge.locked.Book
import com.wizeline.bookchallenge.locked.BooksClient
import com.wizeline.bookchallenge.locked.BooksClientImpl
import com.wizeline.bookchallenge.locked.data
import kotlinx.coroutines.*

class MainActivityViewModel : ViewModel() {

    val topRatedBooksList : MutableLiveData<List<Book>> = MutableLiveData()
    val allBooksList : MutableLiveData<List<Book>> = MutableLiveData()
    val filteredBooksList : MutableLiveData<List<BookWRating>> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()

    private var booksClient: BooksClient = BooksClientImpl()

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + parentJob )

    init{

        loading.postValue(true)
        Handler().let {
            it.postDelayed({
                allBooksList.postValue(data.bookList)
                loading.postValue(false)
            }
            , 1000)
        }
    }


    fun filterBooks(catList : List<String>, catMatch : Int){
        coroutineScope.launch(Dispatchers.Main) {
            loading.postValue(true)
            val booksWRate =  filterSelectedBookCat (catList, catMatch)

            filteredBooksList.postValue(booksWRate)
            loading.postValue(false)
        }
    }
    private suspend fun filterSelectedBookCat (catList : List<String>, catMatch : Int): List<BookWRating> =
        withContext(Dispatchers.Default){

            val booksWRate = mutableListOf<BookWRating>()

            val matchingCats = allBooksList.value?.filter {
                if (catMatch == Constants.EXCAT_CATEGORY_MATCH) {
                    it.categories == catList
                } else {
                    it.categories.containsAll(catList)
                }

            }

            matchingCats?.forEach {
                booksWRate.add(BookWRating(it, booksClient.getRatingForBook(it.id)))
            }
            return@withContext booksWRate
    }

       // filteredBooksList.postValue(booksWRate)
//        Log.e(MainActivityViewModel::class.java.simpleName, "matchingCats:  ${matchingCats?.size} ")


}