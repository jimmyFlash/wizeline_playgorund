package com.wizeline.bookchallenge.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizeline.bookchallenge.MyApplication
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.adapters.EmojiAdapter
import com.wizeline.bookchallenge.databinding.VectorDrawablesFragmentBinding
import com.wizeline.bookchallenge.logic.Emoji
import com.wizeline.bookchallenge.utils.saveFile
import com.wizeline.bookchallenge.views.customeviews.InteractiveImageView
import com.wizeline.bookchallenge.views.customeviews.InteractiveViewPort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class VectorDrawablesFragment : Fragment(), View.OnClickListener, EmojiAdapter.EmojiCallback {

    private lateinit var emojiAdapter: EmojiAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun newInstance() = VectorDrawablesFragment()
    }

    private lateinit var viewModel: VectorDrawablesViewModel

    private lateinit var vectorDrawablesFragmentBinding: VectorDrawablesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vectorDrawablesFragmentBinding = VectorDrawablesFragmentBinding
            .inflate(inflater, container, false)

        setHasOptionsMenu(true)

        vectorDrawablesFragmentBinding.emojiRecyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        emojiAdapter = EmojiAdapter(this@VectorDrawablesFragment)

        vectorDrawablesFragmentBinding.emojiRecyclerView.adapter = emojiAdapter

        vectorDrawablesFragmentBinding.deleteButton.setOnClickListener(this)
        vectorDrawablesFragmentBinding.saveButton.setOnClickListener(this)

        return vectorDrawablesFragmentBinding.root
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =  ViewModelProvider(viewModelStore, viewModelFactory)
                    .get(VectorDrawablesViewModel::class.java)

        setupObserver()
        viewModel.fetchEmojis()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }


    override fun onClick(v: View?) {
       when (v?.id) {
          R.id.deleteButton -> {
            vectorDrawablesFragmentBinding.emojiContainerFrameLayout.removeAllViews()
          }
          R.id.saveButton -> {
            saveBitmapToFile()
          }
        }
    }

    private fun saveBitmapToFile() {
        val bitmap = Bitmap.createBitmap(
            vectorDrawablesFragmentBinding.sceneFrameLayout.width,
            vectorDrawablesFragmentBinding.sceneFrameLayout.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val bgDrawable = vectorDrawablesFragmentBinding.sceneFrameLayout.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        vectorDrawablesFragmentBinding.sceneFrameLayout.draw(canvas)
        val fileName = "${Date().time}_scene.png"
        lifecycleScope.launch(){
            context?.apply {
                saveFile(fileName, bitmap)
                Toast.makeText(this,
                    getString(R.string.file_saved, fileName), Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onEmojiTapped(emoji: Emoji) {
        val image = InteractiveViewPort(this@VectorDrawablesFragment.requireContext(), 5)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT
            /*resources.getDimensionPixelOffset(R.dimen.emoji_size)*/,
            FrameLayout.LayoutParams.MATCH_PARENT
            /*resources.getDimensionPixelOffset(R.dimen.emoji_size)*/
        )
        layoutParams.gravity = Gravity.CENTER
        image.layoutParams = layoutParams
        vectorDrawablesFragmentBinding.emojiContainerFrameLayout.addView(image)
        image.setImageResource(emoji.resourceId)
    }

    @ExperimentalCoroutinesApi
    private fun setupObserver() {
        lifecycleScope.launch {
            val emojiList = viewModel.getEmojis()
            emojiList.collect {
                emojiAdapter.swap(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.vector_drawable_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                TODO("use navigation component to navigate to history view")
                // return true
            }
        }
        return false
    }

}