package com.example.noultestament.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noultestament.R;
import com.example.noultestament.utils.Book;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    private final Book book;

    public ChapterAdapter(Book book) {
        this.book = book;
    }

    @NonNull
    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new ChapterAdapter.ViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ViewHolder holder, int position) {
        holder.chapter.setText(book.getName() + " " + (position + 1));
        if (book.hasNotes(position + 1)) {
            holder.notes.setVisibility(View.VISIBLE);
            holder.notes.setOnClickListener(v -> {

            });
        } else {
            holder.notes.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return book.getChapters();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chapter;
        private final ImageView notes;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull ViewGroup parent) {
            super(parent);
            chapter = parent.findViewById(R.id.name);
            notes = parent.findViewById(R.id.notes);
            layout = parent.findViewById(R.id.book_item);
        }
    }
}
