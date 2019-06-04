package com.wizeline.bookchallenge;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.wizeline.bookchallenge.locked.BooksClient;
import com.wizeline.bookchallenge.locked.BooksClientImpl;

public class MainActivityJava extends AppCompatActivity {

    private BooksClient booksClient = new BooksClientImpl();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(MainActivity.class.getSimpleName(), "Books count: " + booksClient.getTopRatedBooks().size());
    }


}
