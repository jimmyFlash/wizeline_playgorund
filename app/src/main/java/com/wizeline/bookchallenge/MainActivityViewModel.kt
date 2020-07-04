package com.wizeline.bookchallenge

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.bookchallenge.locked.BooksClient
import kotlinx.coroutines.*
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(): ViewModel() {

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScope.launch(Dispatchers.Main) {
            }

            GlobalScope.launch { println("Caught $throwable") }
        }

    val allBooksList : MutableLiveData<List<BookWRating>> = MutableLiveData()// list of all books observable
    val filteredBooksList : MutableLiveData<List<BookWRating>> = MutableLiveData() // list of selected set of books for certain cat.
    val loading : MutableLiveData<Boolean> = MutableLiveData()// loading state observable

    // book service  client instance
    @Inject
    lateinit var booksClient: BooksClient

    private val booksWithRatingFakeStorage by lazy{
        BooksWithRatingFakeStorage.createInstance()
    }

    /*
        setup a base coroutine scope with job
     */
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + parentJob  + coroutineExceptionHandler)

    init{

        loading.postValue(true)// set loading state to true
        // simulate data loaded after 1 sec
        viewModelScope.launch {
            loading.postValue(true)// set loading state to true
            delay(1500)
            allBooksList.postValue(booksWithRatingFakeStorage.getAllBooks())
            loading.postValue(false)// loading complete
        }
    }


    /**
     * method called from activity to load a set of books matching books with certain cat.
     * @param [catList] list of categories selected from cat. spinner
     * @param catMatch Int that indicates whether to match categories list to exact match form all books list
     * or the books that contain the selects catagory list within them
     * example : listOf("Childrens", "Fiction") is contained within listOf("Childrens", "Fiction", "Humor", "Picture books").
     * @see com.wizeline.bookchallenge.Constants
     *
     * runs the computation to filter out the set of books matching the filter criteria
     * posts results to activity through filteredBooksList liveData observable
     */
    fun filterBooks(catList : List<String>, catMatch : Int){
        // launch a base coroutine on main thread
        coroutineScope.launch(Dispatchers.Main) {
            loading.postValue(true)

            val booksWRate =  filterSelectedBookCat (catList, catMatch).sortedByDescending { it.rating }

            filteredBooksList.postValue(booksWRate)
            loading.postValue(false)
        }
    }

    fun getTopRated() = viewModelScope.launch {
        loading.postValue(true)
        filteredBooksList.postValue(booksWithRatingFakeStorage.getTopRatedBooks())
        loading.postValue(false)
    }



    /**
     * suspend function that launches a coroutine that runs CPU-intensive work
     * @param [catList] list of categories selected from cat. spinner
     * @param catMatch Int that indicates whether to match categories list to exact match form all books list
     * @return List of books with ratings
     *
     * Note: ( i decided to generate random ratings for each books every time this method is called
     * rather than create a permanent list with ratings associated with each book permanently like in the
     * data.kt file. While this approach
     * may result in less computation, fact is books like movies etc.. could have their ratings changed with
     * time )
     *
     */
    private suspend fun filterSelectedBookCat (catList : List<String>, catMatch : Int): List<BookWRating> =
        withContext(Dispatchers.Default){

            val booksWRate = mutableListOf<BookWRating>()

            val matchingCats = allBooksList.value?.filter {
                if (catMatch == Constants.EXCAT_CATEGORY_MATCH) {
                    it.b.categories == catList
                } else {
                    it.b.categories.containsAll(catList)
                }

            }
            matchingCats?.forEach {
                val bookRandomRating = booksClient.getRatingForBook(it.b.id)
                booksWRate.add(BookWRating(it.b, bookRandomRating))
            }
            return@withContext booksWRate
    }

}