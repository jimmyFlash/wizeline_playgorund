package com.wizeline.bookchallenge.views

import androidx.lifecycle.ViewModel
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.logic.Emoji
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class VectorDrawablesViewModel @Inject constructor() : ViewModel() {

    @ExperimentalCoroutinesApi
    private val emojis = MutableStateFlow<List<Emoji>>(emptyList())

    @ExperimentalCoroutinesApi
    fun getEmojis(): StateFlow<List<Emoji>> {
        return emojis
    }


    @ExperimentalCoroutinesApi
    fun fetchEmojis(){
        emojis.value = buildEmojiList()
    }

    private fun buildEmojiList(): List<Emoji> {
        val emojiList = ArrayList<Emoji>()
        emojiList.add(Emoji("Baby 1", R.drawable.ic_emoji_baby_skin_0))
        emojiList.add(Emoji("Wings", R.drawable.ic_wings))
        emojiList.add(Emoji("Crown", R.drawable.ic_crown))
        emojiList.add(Emoji("Daisy", R.drawable.ic_daisy))
        emojiList.add(Emoji("Heart", R.drawable.ic_heart))
        emojiList.add(Emoji("Lips", R.drawable.ic_lips))
        emojiList.add(Emoji("Star", R.drawable.ic_star))
        emojiList.add(Emoji("Heart-shades", R.drawable.ic_hearts_shades))
        emojiList.add(Emoji("Crying", R.drawable.ic_crying))
        return emojiList
    }
}