package com.wizeline.bookchallenge.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wizeline.bookchallenge.MyApplication
import com.wizeline.bookchallenge.databinding.ActivityMainBinding
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
