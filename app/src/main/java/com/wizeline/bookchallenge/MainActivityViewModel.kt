package com.wizeline.bookchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.bookchallenge.logic.Intent
import com.wizeline.bookchallenge.logic.State
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject


class MainActivityViewModel @Inject constructor() : ViewModel() {

    private var book: List<BookWRating>? = null

    // state is set by the ViewModel and observed by the View
    var state: State = State.Idel

    //  intentChannel property of type Channel<Intent> for the intents
    val intentChannel = Channel<Intent>(Channel.UNLIMITED)

    //stateChannel of type Channel<State> property for handling states
    private val stateChannel = Channel<State>(Channel.UNLIMITED)

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScope.launch(Dispatchers.Main) {
                println("Caught $throwable")
            }

            //GlobalScope.launch { println("Caught $throwable") }
        }

    val allBooksList: MutableLiveData<List<BookWRating>> =
        MutableLiveData()// list of all books observable
    val filteredBooksList: MutableLiveData<List<BookWRating>> =
        MutableLiveData() // list of selected set of books for certain cat.
    val loading: MutableLiveData<Boolean> = MutableLiveData()// loading state observable

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

        if (state == State.Idel) loading.value = false// set loading state to true

        viewModelScope.launch {
            handleIntents()
        }
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
     * posts results to activity through filteredBooksList liveData observable
     */
    fun filterBooks(catList: List<String>, catMatch: Int) {
        // launch a base coroutine on main thread
        coroutineScope.launch(Dispatchers.Main) {
            loading.postValue(true)

            val booksWRate =
                filterSelectedBookCat(catList, catMatch).sortedByDescending { it.rating }

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

    private suspend fun filterSelectedBookCat(
        catList: List<String>,
        catMatch: Int
    ): List<BookWRating> =
        withContext(Dispatchers.Default) {

            val booksWRate = mutableListOf<BookWRating>()

            val matchingCats = allBooksList.value?.filter {
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

    private fun checkBooksState(bList : List<BookWRating>?): State {
        return if (bList != null) {
            State.BooksLoaded(bList)
        } else {
            State.Error
        }
    }

    private suspend fun setState(reducer: (State) -> State) {
        state = reducer(state)
        when (state) {
           is State.Error -> loading.postValue(false)
           is State.Idel -> loading.postValue(false)
           is State.BooksLoaded -> {
               val booksLs = (state as State.BooksLoaded).booksList
                if (booksLs != null) allBooksList.postValue(booksLs)
                loading.postValue(false)
            }

        }
        stateChannel.send(state)
    }

    private suspend fun loadAssets():List<BookWRating>? =
       withContext(Dispatchers.IO) {
            // simulate data loaded after 1.5 sec
            delay(2000)

            return@withContext booksWithRatingFakeStorage.getAllBooks()
        }


    @ExperimentalCoroutinesApi
    private suspend fun handleIntents() {
        intentChannel.consumeEach { intent ->
            when (intent) {
                Intent.LoadAllBooks -> {
                    loading.postValue(true)// set loading state to true
                    val allBooksList = loadAssets()
                    setState {
                        checkBooksState(allBooksList)
                    }
                }
            }
        }
    }

}