package com.wizeline.bookchallenge

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.bookchallenge.adapters.BookRecyclerAdapter
import com.wizeline.bookchallenge.adapters.spinners.GenericSpinnerAdapter1
import com.wizeline.bookchallenge.databinding.MainFragmentBinding
import com.wizeline.bookchallenge.logic.Intent
import com.wizeline.bookchallenge.logic.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class MainFragment : Fragment() {

    @Inject
    lateinit var mainFragmentViewModelFactory: ViewModelProvider.Factory

    private lateinit var mainFragmentViewModel: MainFragmentViewModel

    private lateinit var mainFrgamentBinding : MainFragmentBinding

    // mutable list of rated books holder
    private var filteredBookCatList: MutableList<BookWRating> = mutableListOf()
    // layout manager for the recycler-view display
    private lateinit var linearLayoutManager: LinearLayoutManager
    // displayed book for a certain category adapter
    private lateinit var adapter: BookRecyclerAdapter

    // list that hold strings representing the book categories extracted form loaded books
    private var catList = mutableListOf<String>()

    private lateinit var adaptsspnr : GenericSpinnerAdapter1

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainFrgamentBinding = MainFragmentBinding.inflate(inflater, container, false)

        // set up the recycler view display and divider
        linearLayoutManager = LinearLayoutManager(activity)
        mainFrgamentBinding.bookList.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(
            mainFrgamentBinding.bookList.context,
            linearLayoutManager.orientation
        )
        mainFrgamentBinding.bookList.addItemDecoration(dividerItemDecoration)

        // instansiate the RV adapter
        adapter = BookRecyclerAdapter()

        mainFrgamentBinding.bookList.adapter = adapter

        return mainFrgamentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainFragmentViewModel =  ViewModelProvider(viewModelStore, mainFragmentViewModelFactory)
            .get(MainFragmentViewModel::class.java)

        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.title = BuildConfig.APP_NAME

        // observe the loading state form VM and update ui
        mainFragmentViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            displayProg(it)
        })

        // register item selected listener to the spinner
        mainFrgamentBinding.catSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when(position){
                    0 -> Unit
                    1 -> mainFragmentViewModel.intentChannel.offer(Intent.LoadTopRated)
                    else -> {
                        val bookCat = catList[position - 1]// get the selected category string
                        // call VM method that filters the list of all books and returns on the matching selected
                        // category or a category that contains this category as subset based on the 2nd argument
                        // constant
                        val stringFilerList = convterStringToList(bookCat)
                        mainFragmentViewModel.filterBooks(
                            stringFilerList,
                            Constants.EXCAT_CATEGORY_MATCH
                        )
                    }

                }
            }
        }

        mainFragmentViewModel.state
            .onEach { state ->  handleState(state)}
            .launchIn(lifecycleScope)


        mainFragmentViewModel.catListSate.observe(viewLifecycleOwner, Observer {
            catList = it as MutableList<String>
            createCustomAdapterSetup(catList,
                savedInstanceState?.getInt("selection") ?: 1)

        })

    }

    /**
     * utility method that displays/hides the loading spinner and and recyclerView to emulate data
     *  being loaded
     *  @param [loading] flag to indicate whether it's loading(true) or not (false)
     */
    private fun displayProg(loading: Boolean){

        if(loading){
            mainFrgamentBinding.progres.visibility = View.VISIBLE
            mainFrgamentBinding.bookList.visibility = View.INVISIBLE
        }else{
            mainFrgamentBinding.progres.visibility = View.GONE
            mainFrgamentBinding.bookList.visibility = View.VISIBLE
        }
    }


    /**
     * helper method that converts a string to list
     * can be converted to extension function!
     * @param [s] the string to convert to List<String>
     * @return List of strings representing the categories for certain book
     */
    fun  convterStringToList(s: String): List<String> {
        return s.split(",").map { it.trim() }
    }

    private fun handleState(state: State) {
        when(state){
            is State.Idle -> Unit
            is State.Error -> handleErrorUi()
            is State.BooksLoaded -> allBooksSuccess(state.booksList)
            is State.TopBooks -> filteredBooksSuccess(state.topBook!!)
            is State.FilterBooks -> filteredBooksSuccess(state.filterList)
            is State.Loading -> Unit
        }
    }

    private fun allBooksSuccess(booksList: List<BookWRating>?) {
        // populate the catList with extracted categories removing duplicates
        mainFragmentViewModel.updateCategories(booksList)
    }

    private fun createCustomAdapterSetup(catList :MutableList<String>, selection: Int = 1){
        adaptsspnr = context?.let { it1 ->
            GenericSpinnerAdapter1(
                it1,
                catList,
                "Choose a category"
            )
        }!!
        mainFrgamentBinding.catSpinner.adapter = adaptsspnr
        // todo keep track of selected spinner index in saved state
        mainFrgamentBinding.catSpinner.setSelection(selection)
    }

    private fun filteredBooksSuccess(booksList: List<BookWRating>) {
        if(booksList.isNotEmpty()) {
            filteredBookCatList.clear()
            filteredBookCatList.addAll(booksList)
            adapter.swap(filteredBookCatList)
        }
    }

    private fun handleErrorUi() {
        TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
        outState.putInt("selection", mainFrgamentBinding.catSpinner.selectedItemPosition)

    }



}