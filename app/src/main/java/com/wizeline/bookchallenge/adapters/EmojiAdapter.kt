package com.wizeline.bookchallenge.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.databinding.RowEmojiBinding
import com.wizeline.bookchallenge.logic.Emoji

class EmojiAdapter (private val emojiCallback: EmojiCallback) :RecyclerView.Adapter<EmojiAdapter.EmojiHolder>(){

    lateinit var inflatedViewBinding : RowEmojiBinding

    private var diffAsync : AsyncListDiffer<Emoji>

    interface EmojiCallback {
        fun onEmojiTapped(emoji: Emoji)
    }

    init {
        // call diff-utils on initialization with defined callback
        val diffUtilCallback = object : DiffUtil.ItemCallback<Emoji>() {

            override fun areItemsTheSame(@NonNull newEmoji: Emoji, @NonNull oldEmoji: Emoji): Boolean {
                return TextUtils.equals(newEmoji.name , oldEmoji.name)
            }

            override fun areContentsTheSame(@NonNull newEmoji: Emoji, @NonNull oldEmoji: Emoji): Boolean {
                return newEmoji.resourceId == oldEmoji.resourceId
            }
        }

        diffAsync = AsyncListDiffer(this, diffUtilCallback  )
    }


    inner class EmojiHolder(private val binding: RowEmojiBinding):RecyclerView.ViewHolder(binding.root){

        /**
         * binds the data object (emoji) with instance of the emoji
         * to populate the ui using dataBinding
         */
        fun bindEmoji(emoji : Emoji){
            binding.emoji = emoji
            itemView.setOnClickListener {
                emojiCallback.onEmojiTapped(emoji)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        inflatedViewBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_emoji, parent, false)
        return EmojiHolder(inflatedViewBinding)
    }

    override fun onBindViewHolder(holder: EmojiHolder, position: Int) {
        val emoji = diffAsync.currentList[position]
        holder.bindEmoji(emoji)
    }

    override fun getItemCount(): Int = diffAsync.currentList.size

    /**
     * in charge of populating / updating the adapter for RV
     * @param [actors] the string to convert to List<String>
     */
    fun swap(actors: List<Emoji>) {
        diffAsync.submitList(actors.toList())
    }

}