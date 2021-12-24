package com.jimmy.rxandroid.data

import android.content.Context
import android.util.Log
import com.jimmy.rxandroid.storage.CheeseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheeseRepository @Inject constructor(val context:Context){

    suspend fun searchCheeseByName(query:String) = withContext(Dispatchers.IO){

        Log.d("Searching", "Searching for $query")
        return@withContext CheeseDatabase.getInstance(context)
            .cheeseDao()
            .findCheese("%$query%")
    }
}