package com.example.noultestament.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noultestament.R;
import com.example.noultestament.activities.ChapterActivity;
import com.example.noultestament.utils.Book;
import com.example.noultestament.utils.Constants;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private final List<Book> books;

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookName.setText(book.getName());
        holder.layout.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ChapterActivity.class);
            intent.putExtra(Constants.BOOK_ORDER, book.getOrder());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookName;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull ViewGroup parent) {
            super(parent);
            bookName = parent.findViewById(R.id.name);
            layout = parent.findViewById(R.id.book_item);
        }
    }
}
