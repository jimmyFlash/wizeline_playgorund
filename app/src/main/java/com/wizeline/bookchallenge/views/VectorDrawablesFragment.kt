package com.wizeline.bookchallenge.views

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizeline.bookchallenge.Constants.READ_EXTERNAL_STORAGE_REQUEST
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

        vectorDrawablesFragmentBinding.loadScreen.apply {
            visibility = View.VISIBLE
        }.also {
            val animatable = it.findViewById<ImageView>(R.id.loadingView).drawable as
                    android.graphics.drawable.Animatable
            animatable.start()
        }

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
                vectorDrawablesFragmentBinding.loadScreen.apply {
                    visibility = View.GONE
                }.also {
                    val animatable = it.findViewById<ImageView>(R.id.loadingView).drawable as
                            android.graphics.drawable.Animatable
                    animatable.stop()
                }
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
            R.id.load_image -> {
                selectImage(requireActivity())
                return true
            }
        }
        return false
    }

    private fun selectImage(activity: Activity) {
        val intent = Intent()
        // Show only images, no videos or anything else
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent ? ) {
        if (resultCode != RESULT_OK) { return }
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST && data != null &&  data.data != null) {
            //We cannot access this Uri directly in android 10
            val selectedImageUri = data.data

            //Later we will use this bitmap to create the File.
            val selectedBitmap: Bitmap? = getBitmap(requireContext(), selectedImageUri!!)

            vectorDrawablesFragmentBinding.sceneryImageView.setImageBitmap(selectedBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getBitmap(context: Context, imageUri: Uri): Bitmap? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    imageUri))
        } else {
            context
                .contentResolver
                .openInputStream(imageUri) ?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }

}