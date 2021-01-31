package com.wizeline.bookchallenge

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class VectorDrawablesFragment : Fragment() {

    companion object {
        fun newInstance() = VectorDrawablesFragment()
    }

    private lateinit var viewModel: VectorDrawablesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vector_drawables_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VectorDrawablesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}