package com.wizeline.bookchallenge.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.bookchallenge.Constants
import com.wizeline.bookchallenge.logic.BookWRating
import com.wizeline.bookchallenge.logic.BooksWithRatingFakeStorage
import com.wizeline.bookchallenge.logic.Intent
import com.wizeline.bookchallenge.logic.State
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(): ViewModel() {

    // state is set by the ViewModel and observed by the View
    private var bookState: State = State.Idle

    //  intentChannel property of type Channel<Intent> for the intents
    val intentChannel = Channel<Intent>(Channel.UNLIMITED)

    /*
        Changing the state no longer requires a suspending method
        StateFlow inherits all the benefits of the Flow
        Nothing gets emitted if there is no subscriber. If we were using basic channels,
        even if there was no listener, the events would pop up every time they got fired.
     */
    @ExperimentalCoroutinesApi
    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State>
        get() = _state

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScope.launch(Dispatchers.Main) {
                println("Caught $throwable")
            }

            //GlobalScope.launch { println("Caught $throwable") }
        }

    private val loading: MutableLiveData<Boolean> = MutableLiveData()// loading state observable
    private val catsList: MutableLiveData<List<String>> = MutableLiveData()// loading state observable

    val loadingState : LiveData<Boolean>
        get() = loading

    val catListSate : LiveData<List<String>>
        get() = catsList

// book service  client instance
//    @Inject
//    lateinit var booksClient: BooksClient

    @Inject
    lateinit var booksWithRatingFakeStorage: BooksWithRatingFakeStorage

    /*
        setup a base coroutine scope with job
     */
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + parentJob + coroutineExceptionHandler
    )

    init {

        intentChannel.trySend(Intent.LoadAllBooks).isSuccess

        if (bookState == State.Idle) loading.value = false// set loading state to true

        viewModelScope.launch {
            handleIntents()
        }
    }

    fun updateCategories(loadedCatLst : List<BookWRating>?){

        // populate the catList with extracted categories removing duplicates
        val sortedCategories = loadedCatLst?.let {
            it.map { bookWrating ->
                bookWrating.b.categories.joinToString()
            }
                .distinct()
                .sortedDescending()
                .reversed() as MutableList<String>

        }
        sortedCategories?.add(0, "Top Rated Books")
        catsList.value = sortedCategories!!
    }


    /**
     * method called from activity to load a set of books matching books with certain cat.
     * @param [catList] list of categories selected from cat. spinner
     * @param catMatch Int that indicates whether to match categories list to exact match form all books list
     * or the books that contain the selects catagory list within them
     * example : listOf("Childrens", "Fiction") is contained within listOf("Children's", "Fiction", "Humor", "Picture books").
     * @see com.wizeline.bookchallenge.Constants
     *
     * runs the computation to filter out the set of books matching the filter criteria
     */
    fun filterBooks(catList: List<String>, catMatch: Int) {
        // launch a base coroutine on main thread
        intentChannel.offer(Intent.FilterBooks(catList, catMatch))
    }

    private suspend fun  getTopRated():List<BookWRating>? =  withContext(Dispatchers.IO){
        return@withContext booksWithRatingFakeStorage.getTopRatedBooks()
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

    private suspend fun filterSelectedBookCat(
        catList: List<String>,
        catMatch: Int
    ): List<BookWRating> =
        withContext(Dispatchers.Default) {

            val booksWRate = mutableListOf<BookWRating>()

            val matchingCats = booksWithRatingFakeStorage.getAllBooks()?.filter {
                if (catMatch == Constants.EXCAT_CATEGORY_MATCH) {
                    it.b.categories == catList
                } else {
                    it.b.categories.containsAll(catList)
                }

            }
            matchingCats?.forEach {
                val bookRandomRating = booksWithRatingFakeStorage.getRatingForBook(it.b.id) ?: 0F
                booksWRate.add(BookWRating(it.b, bookRandomRating))
            }
            return@withContext booksWRate
        }

    private fun checkBooksState(bList : List<BookWRating>?, intent : Intent): State {
        loading.postValue(false)
        return if (bList != null) {
            when (intent){
                Intent.LoadAllBooks ->  State.BooksLoaded(bList)
                Intent.LoadTopRated ->  State.TopBooks(bList)
                Intent.FilterBooks() -> State.FilterBooks(bList)
                else -> State.Idle
            }

        } else {
            State.Error
        }
    }

    private suspend fun loadAssets():List<BookWRating>? =
        withContext(Dispatchers.IO) {
            // simulate data loaded after 2 sec
            delay(2000)
            return@withContext booksWithRatingFakeStorage.getAllBooks()
        }


    @ExperimentalCoroutinesApi
    private suspend fun handleIntents() {
        intentChannel.consumeEach { intent ->
            when (intent) {

                is  Intent.LoadAllBooks -> {
                    loading.postValue(true)// set loading state to true
                    val allBooksList = loadAssets()
                    _state.value = checkBooksState(allBooksList, Intent.LoadAllBooks)
                }
                is   Intent.LoadTopRated->{
                    loading.postValue(true)// set loading state to true
                    val topBooks =  getTopRated()
                    _state.value = checkBooksState(topBooks, Intent.LoadTopRated)
                }
                is  Intent.FilterBooks->{
                    loading.postValue(true)// set loading state to true
                    val filteredBooks =  filterSelectedBookCat(intent.catList!!, intent.catMatch!!)
                        .sortedByDescending { it.rating }
                    _state.value = checkBooksState(filteredBooks,  Intent.FilterBooks())
                }
            }
        }
    }

}