package com.lovib.noultestament.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lovib.noultestament.R;
import com.lovib.noultestament.adapters.NoteAdapter;
import com.lovib.noultestament.utils.Book;
import com.lovib.noultestament.utils.Constants;
import com.lovib.noultestament.utils.Note;
import com.lovib.noultestament.utils.Storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class NotesActivity extends AppCompatActivity {
    private Storage storage = Storage.getInstance();
    private int order, chapter, atTime;
    private boolean addNote;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        order = getIntent().getIntExtra(Constants.BOOK_ORDER, 0);
        chapter = getIntent().getIntExtra(Constants.CHAPTER, 1);
        addNote = getIntent().getBooleanExtra(Constants.ADD_NOTE, false);
        atTime = getIntent().getIntExtra(Constants.AT_TIME, 0);
        book = Storage.getInstance().getBook(order);
        if (book != null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            TextView title = toolbar.findViewById(R.id.title);
            title.setText(book.getName() + " " + chapter);
            ImageView back = toolbar.findViewById(R.id.back);
            back.setOnClickListener(v -> onBackPressed());
            configureNotes();
            RecyclerView recyclerView = findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            NoteAdapter adapter = new NoteAdapter(order, chapter, addNote);
            recyclerView.setAdapter(adapter);
        }
    }

    private void configureNotes() {
        if (storage.getNotes(order, chapter) == null) {
            storage.createEmptyArrayAtKey(order, chapter);
        }
        ArrayList<Note> notes = storage.getNotes(order, chapter);
        notes.sort(Comparator.comparingInt(Note::getCharacter));
        char nextCharacter = nextCharacter(notes);
        if (addNote && !storage.existNoteAtTime(order, chapter, atTime, 0)) {
            notes.add(0, new Note(nextCharacter, atTime));
        }
        storage.saveNotes(this);
    }

    private char nextCharacter(ArrayList<Note> notes) {
        char nextCharacter = 'A';
        for (Note note : notes) {
            if (note.getCharacter() == nextCharacter) {
                nextCharacter++;
            } else {
                break;
            }
        }
        return nextCharacter;
    }
}
