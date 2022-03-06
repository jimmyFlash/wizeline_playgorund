package com.wizeline.bookchallenge.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.wizeline.bookchallenge.R
import com.wizeline.bookchallenge.databinding.FragmentNavigationBinding


/**
 * A simple [Fragment] subclass.
 * Use the [NavigationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationFragment : Fragment(), View.OnClickListener {

    lateinit var fragmentNavigationBinding: FragmentNavigationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        fragmentNavigationBinding = FragmentNavigationBinding.inflate(inflater, container, false)
        fragmentNavigationBinding.mviImplement.setOnClickListener(this)
        fragmentNavigationBinding.vectorDrawMeme.setOnClickListener(this)
        fragmentNavigationBinding.annotationGen.setOnClickListener(this)
        fragmentNavigationBinding.rxAndroid.setOnClickListener(this)
        return fragmentNavigationBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NavigationFragment.
         */
        @JvmStatic
        fun newInstance() {

        }
    }

    override fun onClick(v: View) {
        val action: NavDirections
        when(v.id){
            R.id.mviImplement ->{
                 action = NavigationFragmentDirections.firstAction()
                findNavController().navigate(action)
            }
            R.id.vectorDrawMeme ->{
                 action = NavigationFragmentDirections.secondAction()
                findNavController().navigate(action)
            }
            R.id.annotation_gen ->{
                action = NavigationFragmentDirections.thirdAction()
                findNavController().navigate(action)
            }
            R.id.rx_android->{
                action = NavigationFragmentDirections.forthAction()
                findNavController().navigate(action)
            }
        }
    }
}