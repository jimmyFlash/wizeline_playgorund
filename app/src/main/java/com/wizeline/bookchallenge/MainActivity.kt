package com.wizeline.bookchallenge

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.bookchallenge.adapters.BookRecyclerAdapter
import com.wizeline.bookchallenge.adapters.spinners.GenericSpinnerAdapter1
import com.wizeline.bookchallenge.databinding.ActivityMainBinding
import com.wizeline.bookchallenge.logic.Intent
import com.wizeline.bookchallenge.logic.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    // VM reference
    @Inject
    lateinit var mainActivityViewModelFactory: ViewModelProvider.Factory

    lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grabs instance of the application graph
        // and populates @Inject fields with objects from the graph
        (application as MyApplication).appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainActivityViewModel =  ViewModelProvider(viewModelStore, mainActivityViewModelFactory)
                                .get(MainActivityViewModel::class.java)
    }

}
