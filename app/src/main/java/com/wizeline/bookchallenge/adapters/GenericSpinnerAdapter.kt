package com.wizeline.bookchallenge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.wizeline.bookchallenge.R


class GenericSpinnerAdapter(val cntxt : Context, val bookCat : List<String>) : BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val mViewHolder: ViewHolder?
        val inflater = cntxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view : View? = convertView
        if(view == null){
            view = inflater.inflate(R.layout.book_cat_drop, null)
            mViewHolder = ViewHolder(view.findViewById(R.id.cat_desc_1))
            mViewHolder.tv1.text =  bookCat[position]
            view.tag = mViewHolder
        }else{

            mViewHolder = convertView?.tag as ViewHolder?
        }

        view?.findViewById<TextView>(R.id.cat_desc_1)?.text = mViewHolder?.tv1?.text

        return view!!
    }

    override fun getItem(position: Int): Any = bookCat[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = bookCat.size

    class ViewHolder(val tv1 : TextView)
}