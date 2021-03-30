package com.wizeline.bookchallenge.views

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.bookchallenge.logic.Emoji
import com.wizeline.bookchallenge.logic.HistoryItem
import com.wizeline.bookchallenge.utils.readFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryViewModel @Inject constructor(): ViewModel() {



    @ExperimentalCoroutinesApi
    private val historyImages = MutableStateFlow<List<HistoryItem>>(emptyList())

    @ExperimentalCoroutinesApi
    fun getSavedImages(): StateFlow<List<HistoryItem>> {
        return historyImages
    }


    @ExperimentalCoroutinesApi
    suspend fun loadImages(context : Context) = withContext(Dispatchers.Main) {
        context.readFiles().apply {
            historyImages.value = this
        }
    }

}