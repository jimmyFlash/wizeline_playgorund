package com.jimmy.rxandroid.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimmy.rxandroid.data.CheeseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheeseViewModel @Inject constructor(val repository : CheeseRepository): ViewModel() {

    fun loadCheeseByName(cheeseName:String){
        Log.d(CheeseViewModel::class.java.simpleName, "LOADED VIEWMODLE")
        viewModelScope.launch {
            repository.searchCheeseByName(cheeseName)
        }
    }

    
}