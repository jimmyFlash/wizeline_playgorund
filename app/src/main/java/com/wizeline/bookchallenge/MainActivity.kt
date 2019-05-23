package com.wizeline.bookchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wizeline.bookchallenge.locked.BooksClientImpl

class MainActivity : AppCompatActivity() {

    private val booksClient = BooksClientImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e(MainActivity::class.java.simpleName, "Books count: ${booksClient.getTopRatedBooks().size}")

    }
}
