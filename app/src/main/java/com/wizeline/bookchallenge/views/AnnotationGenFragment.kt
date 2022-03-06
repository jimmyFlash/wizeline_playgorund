package com.wizeline.bookchallenge.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jimmy.annotation.GreetingGenerator
import com.wizeline.bookchallenge.databinding.FragmentAnnotationGenBinding


/**
 * A simple [Fragment] subclass.
 * Use the [AnnotationGenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnnotationGenFragment : Fragment() {

    private lateinit var fragmentAnnotationGenBinding: FragmentAnnotationGenBinding

    val TAG = AnnotationGenFragment::class.java.simpleName

    @GreetingGenerator
    class SantaGreeting

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        fragmentAnnotationGenBinding = FragmentAnnotationGenBinding.inflate(
            inflater, container, false)

        fragmentAnnotationGenBinding.greetingTxt.text = Generated_SantaGreeting().greeting()
        return fragmentAnnotationGenBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AnnotationGenFragment.
         */
        @JvmStatic
        fun newInstance() = AnnotationGenFragment()
    }
}