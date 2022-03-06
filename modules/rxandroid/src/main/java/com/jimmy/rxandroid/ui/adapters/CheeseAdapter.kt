package com.jimmy.rxandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jimmy.rxandroid.R
import com.jimmy.rxandroid.storage.Cheese
import com.jimmy.rxandroid.storage.CheeseDatabase
import com.jimmy.rxandroid.ui.customview.CheckableImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class CheeseAdapter : ListAdapter<Cheese, CheeseAdapter.ViewHolder>(CheeseDiff) {

  var compositeDisposable = CompositeDisposable()

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
    compositeDisposable.clear()
  }

  override fun onBindViewHolder(holder: CheeseAdapter.ViewHolder, position: Int) {
    val cheese = getItem(position)
    holder.bind(cheese)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseAdapter.ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,
      false))
  }

  override fun getItemViewType(position: Int): Int {
    return position
  }


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind (cheese : Cheese ){

        val cheeseName = itemView.findViewById<TextView>(R.id.textView)
        val imageFavorite = itemView.findViewById<CheckableImageView>(R.id.imageFavorite)

        cheeseName.text = cheese.name
        imageFavorite.isChecked = cheese.favorite == 1

        compositeDisposable.add(
            // A Maybe is a computation that emits either a single value, no value or an error.
            // They are good for things such as database updates and deletes
            Maybe.create<Boolean> { emitter ->
              emitter.setCancellable {
                imageFavorite.setOnClickListener(null)
              }

             imageFavorite.setOnClickListener {
                emitter.onSuccess((it as CheckableImageView).isChecked) // Emit the checked state on success
              }
            }.toFlowable().onBackpressureLatest() // Turn the Maybe into a flowable
              .observeOn(Schedulers.io())
              .map { isChecked ->
                cheese.favorite = if (!isChecked) 1 else 0
                val database = CheeseDatabase.getInstance(itemView.context).cheeseDao()
                database.favoriteCheese(cheese) // Perform the update on the Cheeses table
                cheese.favorite // Return the result of the operation
              }
              .subscribeOn(AndroidSchedulers.mainThread())
              .subscribe {
                imageFavorite.isChecked = it == 1 // Use the result from the emission to change the outline to a filled in heart
              }
        )
    }
  }

  companion object CheeseDiff : DiffUtil.ItemCallback<Cheese>() {

    override fun areItemsTheSame(@NonNull CheeseNew: Cheese,
                                 @NonNull CheeseOld: Cheese  ): Boolean {
      return   CheeseOld == CheeseNew
    }

    override fun areContentsTheSame(@NonNull CheeseNew: Cheese,
                                    @NonNull CheeseOld: Cheese  ) = CheeseOld.id == CheeseNew.id

  }

}