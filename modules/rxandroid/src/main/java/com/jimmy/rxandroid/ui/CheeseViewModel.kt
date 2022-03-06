package com.jimmy.rxandroid.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimmy.rxandroid.data.CheeseRepository
import com.jimmy.rxandroid.storage.Cheese
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheeseViewModel @Inject constructor(private val repository : CheeseRepository): ViewModel() {


    private val dataInsertedMutable = MutableLiveData<Boolean>()
    val dataInserted:LiveData<Boolean> = dataInsertedMutable


    private val cheeseQueryList = MutableLiveData<List<Cheese> >()
    val cheeseQuery:LiveData<List<Cheese>> = cheeseQueryList


    fun loadCheeseByName(cheeseName:String) {
        Log.d(CheeseViewModel::class.java.simpleName, "LOADED VIEWMODLE")
        viewModelScope.launch {
            delay(2000)
            cheeseQueryList.value =  repository.searchCheeseByName(cheeseName)
        }
    }

    fun searchCheeseName(cheeseName:String) = repository.searchCheeseByNameRx(cheeseName)


    /**
     * populate Db with initial dummy data
     * and observe live data
     */
    fun loadInitData() =  repository.loadInitialData(onComplete = {
        Log.d(CheeseViewModel::class.java.simpleName,
            "Cheeses have been inserted into the database!")
        dataInsertedMutable.value = true
    }){
        Log.d(CheeseViewModel::class.java.simpleName, "Error inserting cheeses!")
        dataInsertedMutable.value = false
    }


}