package com.wizeline.bookchallenge.adapters.spinners

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import com.wizeline.bookchallenge.databinding.PromptItemBinding
import com.wizeline.bookchallenge.databinding.SpinnerItemBinding

class GenericSpinnerAdapter1(
    private var elements: List<String>,
    private val prompt: String
) : BaseAdapter() {

    private lateinit var spinnerItemBinding: SpinnerItemBinding
    private lateinit var spinnerPromptBinding: PromptItemBinding

     private var backing : IntArray =  IntArray(elements.size + 1)

    override fun getCount(): Int {
        return backing.size
    }

    override fun getItem(position: Int): Any? {
        return if (position == 0) null else elements[position-1]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun isEnabled(position: Int): Boolean = position != 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemsViewHolder : ItemsViewHolder

        if(position == 0){

            spinnerPromptBinding = PromptItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false)
            itemsViewHolder = ItemsViewHolder(spinnerPromptBinding)
        }else{
            spinnerItemBinding= SpinnerItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false)
            itemsViewHolder = ItemsViewHolder(spinnerItemBinding)
        }
        itemsViewHolder.view.tag = itemsViewHolder
        itemsViewHolder.bind(getItem(position))
        return itemsViewHolder.view
    }

    fun updateList(newList : List<String>){
        elements = newList
        backing  =  IntArray(elements.size + 1)
        notifyDataSetChanged()
    }


    inner class ItemsViewHolder(private val binding : ViewBinding){

        val view :View
            get() = binding.root

        fun bind (titleObj : Any?) {

            when (binding ) {
                is PromptItemBinding -> {
                    binding.prompt.text = prompt
                }
               is SpinnerItemBinding  -> {
                    binding.item.text = titleObj as String
                }
            }
        }
    }
}