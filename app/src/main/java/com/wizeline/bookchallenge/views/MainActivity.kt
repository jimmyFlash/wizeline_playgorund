package com.wizeline.bookchallenge.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.wizeline.bookchallenge.Constants
import com.wizeline.bookchallenge.MyApplication
import com.wizeline.bookchallenge.R
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

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment? ?: return

        val navController = host.navController

        mainActivityViewModel =  ViewModelProvider(viewModelStore, mainActivityViewModelFactory)
                                .get(MainActivityViewModel::class.java)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent? ) {
        if (resultCode != RESULT_OK) { return }
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_REQUEST &&
            data != null &&  data.data != null) {
            val activeFragment  = this.supportFragmentManager.findFragmentById(R.id.navHostFragment)
                ?.childFragmentManager?.primaryNavigationFragment

            if (activeFragment is VectorDrawablesFragment){
                activeFragment.onActivityResult(requestCode, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
