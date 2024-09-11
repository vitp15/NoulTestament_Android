package com.example.noultestament.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noultestament.R;
import com.example.noultestament.utils.Note;
import com.example.noultestament.utils.Storage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private boolean addNote;
    private final Storage storage;
    private final ArrayList<Note> notes;

    public NoteAdapter(int order, int chapter, boolean addNote) {
        this.addNote = addNote;
        storage = Storage.getInstance();
        if (storage.getNotes(order, chapter) == null) {
            storage.createEmptyArrayAtKey(order, chapter);
        }
        notes = storage.getNotes(order, chapter);
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note note = notes.get(position);
        if (position == 0 && addNote) {
            editMode(holder);
            addNote = false;
        } else {
            commonNote(holder, note);
        }
        holder.character.setText(String.valueOf(note.getCharacter()));
        holder.message.setText(note.getMessage());
        holder.editMessage.setText(note.getMessage());
        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.delete.getContext());
            builder.setTitle(R.string.delete_note_title)
                    .setMessage(R.string.delete_note_message)
                    .setPositiveButton(R.string.delete_note_button, (dialog, which) -> {
                        notes.remove(note);
                        storage.saveNotes(holder.itemView.getContext());
                        notifyDataSetChanged();
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.delete_note_cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(true)
                    .show();
        });
        holder.editMessage.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                note.setMessage(holder.editMessage.getText().toString());
                commonNote(holder, note);
                storage.saveNotes(holder.itemView.getContext());
            }
        });
        holder.editMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                note.setMessage(holder.editMessage.getText().toString());
                commonNote(holder, note);
                storage.saveNotes(holder.itemView.getContext());
                return true;
            }
            return false;
        });
        holder.edit.setOnClickListener(v -> {
            editMode(holder);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView character, message;
        private final ImageView edit, delete;
        private final EditText editMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            character = itemView.findViewById(R.id.character);
            message = itemView.findViewById(R.id.message);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            editMessage = itemView.findViewById(R.id.message_edit_text);
        }
    }

    private void editMode(@NonNull NoteAdapter.ViewHolder holder) {
        holder.message.setVisibility(View.GONE);
        holder.editMessage.setVisibility(View.VISIBLE);
        holder.edit.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
        holder.editMessage.requestFocus();
    }

    private void commonNote(@NonNull NoteAdapter.ViewHolder holder, Note note) {
        holder.message.setText(note.getMessage());
        holder.message.setVisibility(View.VISIBLE);
        holder.editMessage.setVisibility(View.GONE);
        holder.edit.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);
    }
}
