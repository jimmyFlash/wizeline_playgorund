package com.wizeline.bookchallenge.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wizeline.bookchallenge.logic.BookWRating
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.databinding.RecyclerviewItemRowBinding

class BookRecyclerAdapter : RecyclerView.Adapter<BookRecyclerAdapter.BookHolder>(){

    private var diffAsync : AsyncListDiffer<BookWRating>


    init{

        // call diff-utils on initialization with defined callback
        val diffUtilCallback = object : DiffUtil.ItemCallback<BookWRating>() {

            override fun areItemsTheSame(@NonNull newBook: BookWRating, @NonNull oldBook: BookWRating): Boolean {
                return TextUtils.equals(newBook.b.id , oldBook.b.id)
            }

            override fun areContentsTheSame(@NonNull newBook: BookWRating, @NonNull oldBook: BookWRating): Boolean {
                return newBook.rating == oldBook.rating
            }
        }

        diffAsync = AsyncListDiffer(this, diffUtilCallback  )

    }

    /**
     * in charge of populating / updating the adapter for RV
     * @param [actors] the string to convert to List<String>
     */
    fun swap(actors: MutableList<BookWRating>) {
        diffAsync.submitList(actors.toList())
    }

    // binding ref
    lateinit var inflatedViewBinding : RecyclerviewItemRowBinding


    // Viewholder implementation
    class BookHolder(private var binding: RecyclerviewItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val view = binding.root

        /**
         * binds the data object (bookRating) with instance of the BookWRating
         * to populate the ui using dataBinding
         */
        fun bindBook(book: BookWRating) {
            binding.bookRating = book

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        inflatedViewBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.recyclerview_item_row, parent, false)
        return BookHolder(inflatedViewBinding)

    }

    override fun getItemCount(): Int = diffAsync.currentList.size

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book = diffAsync.currentList[position]
        holder.bindBook(book)
    }


}