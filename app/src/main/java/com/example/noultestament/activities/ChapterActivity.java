package com.example.noultestament.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noultestament.R;
import com.example.noultestament.adapters.BookAdapter;
import com.example.noultestament.adapters.ChapterAdapter;
import com.example.noultestament.utils.Book;
import com.example.noultestament.utils.Storage;

import java.util.Objects;

public class ChapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        Book book = (Book) getIntent().getSerializableExtra("book");
        if (book != null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            TextView title = toolbar.findViewById(R.id.title);
            title.setText(book.getName());
            ImageView back = toolbar.findViewById(R.id.back);
            back.setOnClickListener(v -> onBackPressed());
            RecyclerView recyclerView = findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ChapterAdapter adapter = new ChapterAdapter(book);
            recyclerView.setAdapter(adapter);
        }
    }
}