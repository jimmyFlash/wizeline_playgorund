package com.wizeline.bookchallenge.adapters

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.databinding.RowHistoryBinding
import com.wizeline.bookchallenge.logic.HistoryItem
import java.io.File


class HistoryAdapter :RecyclerView.Adapter<HistoryAdapter.HistoryItemHolder>(){

    lateinit var inflatedViewBinding : RowHistoryBinding

    private var diffAsync : AsyncListDiffer<HistoryItem>


    init {
        // call diff-utils on initialization with defined callback
        val diffUtilCallback = object : DiffUtil.ItemCallback<HistoryItem>() {

            override fun areItemsTheSame(@NonNull newHistoryItem: HistoryItem,
                                         @NonNull oldHistoryItem: HistoryItem): Boolean {
                return TextUtils.equals(newHistoryItem.filePath , oldHistoryItem.filePath)
            }

            override fun areContentsTheSame(@NonNull newHistoryItem: HistoryItem,
                                            @NonNull oldHistoryItem: HistoryItem): Boolean {
                return newHistoryItem.fileName == oldHistoryItem.fileName
            }
        }
        diffAsync = AsyncListDiffer(this, diffUtilCallback  )
    }


    inner class HistoryItemHolder(private val binding: RowHistoryBinding):
        RecyclerView.ViewHolder(binding.root){

        val imageView = binding.historyImageView
        val textView = binding.historyFileNameTextView
        val imageButton = binding.historyShareImageButton

        fun bindHistoryItem(historyItem : HistoryItem){


            Picasso.get()
                .load(File(historyItem.filePath))
                .into(imageView)

            textView.text = historyItem.fileName

            imageButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                val fileDir = it.context.filesDir
                val emojiDir = File(fileDir, "/emojis")
                if (emojiDir.exists()) {
                    val file = File(emojiDir, historyItem.fileName)
                    val fileUri: Uri? = try {
                        FileProvider.getUriForFile(
                            it.context, "com.wizeline.bookchallenge.fileprovider", file
                        )
                    } catch (e: IllegalArgumentException) {
                        Log.e("File Selector", "The selected file can't be shared: $file")
                        null
                    }
                    if (fileUri != null) {
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                        intent.type = it.context.contentResolver.getType(fileUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        it.context.startActivity(
                            Intent.createChooser(intent, it.context.getString(R.string.share_file))
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        inflatedViewBinding = RowHistoryBinding.inflate(layoutInflater,
            parent, false)
        return HistoryItemHolder(inflatedViewBinding)
    }

    override fun onBindViewHolder(holder: HistoryItemHolder, position: Int) {
        val historyItem = diffAsync.currentList[position]
        holder.bindHistoryItem(historyItem)
    }

    override fun getItemCount(): Int = diffAsync.currentList.size

    /**
     * in charge of populating / updating the adapter for RV
     * @param [actors] the string to convert to List<String>
     */
    fun swap(actors: List<HistoryItem>) {
        diffAsync.submitList(actors.toList())
    }

}