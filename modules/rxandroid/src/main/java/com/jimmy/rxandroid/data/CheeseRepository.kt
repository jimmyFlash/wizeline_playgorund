package com.jimmy.rxandroid.data

import android.content.Context
import android.util.Log
import com.jimmy.rxandroid.storage.Cheese
import com.jimmy.rxandroid.storage.CheeseDatabase
import com.jimmy.rxandroid.storage.CheeseUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheeseRepository @Inject constructor(val context:Context){

    /**
     * query room db for cheese with similar names, coroutine version
     */
    suspend fun searchCheeseByName(query:String) = withContext(Dispatchers.IO){
        Log.d("Searching", "Searching for $query")
        return@withContext CheeseDatabase.getInstance(context)
            .cheeseDao()
            .findCheese("%$query%")
    }

    /**
     * query room db for cheese with similar names
     */
    fun searchCheeseByNameRx(query:String) : List<Cheese> {
        Log.d("Searching", "Searching for $query")
        return CheeseDatabase.getInstance(context)
            .cheeseDao()
            .findCheese("%$query%")
    }

    /**
     * load cheese names form static data and converts them into Cheese object inserting them
     * into room db
     * @param onComplete lambda called in success
     * @param onError lambda called to handle error
     */
    fun loadInitialData(onComplete: () -> Unit, onError:() -> Unit): Flowable<List<Long>> {
        // Maybe will return a deferred single emission of success or error
        return Maybe.fromAction<List<Long>> {

            val database = CheeseDatabase.getInstance(context = context).cheeseDao()

            // load and populate the cheeseList instances of Cheese object
            // from CheeseUtil.CHEESES array of cheese names
            val cheeseList = arrayListOf<Cheese>()
            for (cheese in CheeseUtil.CHEESES) {
                cheeseList.add(Cheese(name = cheese))
            }
            // insert bulk into db
            database.insertAll(cheeseList)

        }.toFlowable() // convert to flowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            // when completes successfully call lambda onComplete
            .doOnComplete {
                onComplete()
            }
            // when fails call lambda onError
            .doOnError {
                onError()
            }
    }
}