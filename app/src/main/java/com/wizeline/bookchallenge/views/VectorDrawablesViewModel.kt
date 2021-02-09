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
        emojiList.add(Emoji("Baby 2", R.drawable.ic_emoji_baby_skin_1))
        emojiList.add(Emoji("Baby 3", R.drawable.ic_emoji_baby_skin_2))
        emojiList.add(Emoji("Baby 4", R.drawable.ic_emoji_baby_skin_3))
        emojiList.add(Emoji("Baby 5", R.drawable.ic_emoji_baby_skin_4))
        emojiList.add(Emoji("Baby 6", R.drawable.ic_emoji_baby_skin_5))
        emojiList.add(Emoji("Baby 7", R.drawable.ic_emoji_baby_skin_6))
        emojiList.add(Emoji("Baby 8", R.drawable.ic_emoji_baby_skin_7))
        emojiList.add(Emoji("Baby 9", R.drawable.ic_emoji_baby_skin_8))
        return emojiList
    }
}