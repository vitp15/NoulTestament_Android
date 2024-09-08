package com.example.noultestament;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noultestament.activities.AudioActivity;
import com.example.noultestament.adapters.BookAdapter;
import com.example.noultestament.utils.Constants;
import com.example.noultestament.utils.Storage;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        String forceClosed = Storage.getInstance().getForceClosed(this);
        if (forceClosed != null && !forceClosed.isEmpty()) {
            Storage.getInstance().removeForceClosed(this);
            String[] elements = forceClosed.split("_");
            if (elements.length == 2) {
                try {
                    int order = Integer.parseInt(elements[0]);
                    int chapter = Integer.parseInt(elements[1]);
                    Intent intent = new Intent(this, AudioActivity.class);
                    intent.putExtra(Constants.BOOK_ORDER, order);
                    intent.putExtra(Constants.CHAPTER, chapter);
                    intent.putExtra(Constants.FORCE_CLOSED, true);
                    startActivity(intent);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookAdapter adapter = new BookAdapter(Storage.getInstance().getBooks());
        recyclerView.setAdapter(adapter);
    }
}
