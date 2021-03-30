package com.wizeline.bookchallenge.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizeline.bookchallenge.MyApplication
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.adapters.EmojiAdapter
import com.wizeline.bookchallenge.databinding.VectorDrawablesFragmentBinding
import com.wizeline.bookchallenge.logic.Emoji
import com.wizeline.bookchallenge.utils.saveFile
import com.wizeline.bookchallenge.views.customeviews.InteractiveViewPort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class VectorDrawablesFragment : Fragment(), View.OnClickListener,
    EmojiAdapter.EmojiCallback {

    private lateinit var emojiAdapter: EmojiAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun newInstance() = VectorDrawablesFragment()
    }

    private lateinit var viewModel: VectorDrawablesViewModel

    private lateinit var vectorDrawablesFragmentBinding: VectorDrawablesFragmentBinding

    private lateinit var options: NavOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vectorDrawablesFragmentBinding = VectorDrawablesFragmentBinding
            .inflate(inflater, container, false)

        (requireActivity() as? MainActivity)?.
                setSupportActionBar(vectorDrawablesFragmentBinding.mainToolbar)



        vectorDrawablesFragmentBinding.emojiRecyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        emojiAdapter = EmojiAdapter(this@VectorDrawablesFragment)

        vectorDrawablesFragmentBinding.emojiRecyclerView.adapter = emojiAdapter

        vectorDrawablesFragmentBinding.deleteButton.setOnClickListener(this)
        vectorDrawablesFragmentBinding.saveButton.setOnClickListener(this)

        return vectorDrawablesFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // add navigation transition animation options
         options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
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

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.vector_drawable_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                // option 1
                //findNavController().navigate(R.id.historyImageFragment, null, options)

                //option 2
                //Navigation.createNavigateOnClickListener(R.id.next_action, null)

                // option 3
                /*
                    Navigation by actions has the following benefits over navigation by destination:

                    You can visualize the navigation paths through your app

                    Actions can contain additional associated attributes you can set,
                    such as a transition animation, arguments values, and backstack behavior

                    You can use the plugin safe args to navigate
                */
                val action = VectorDrawablesFragmentDirections.nextAction()
                findNavController().navigate(action)

                return true
            }
        }
        return false
    }

}