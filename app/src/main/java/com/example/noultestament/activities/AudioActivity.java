package com.example.noultestament.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noultestament.R;
import com.example.noultestament.utils.Book;
import com.example.noultestament.utils.Storage;

public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        int order = getIntent().getIntExtra("bookOrder", 0);
        Book book = Storage.getInstance().getBook(order);
        if (book != null) {

        }
    }
}