package com.lovib.noultestament.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.lovib.noultestament.R;
import com.lovib.noultestament.activities.AudioActivity;
import com.lovib.noultestament.utils.Constants;
import com.lovib.noultestament.utils.Note;
import com.lovib.noultestament.utils.Storage;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private boolean addNote;
    private final Storage storage;
    private final ArrayList<Note> notes;
    private final int order, chapter;

    public NoteAdapter(int order, int chapter, boolean addNote) {
        this.order = order;
        this.chapter = chapter;
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
        holder.noteIcon.setOnClickListener(v -> goToAudio(holder, note));
        holder.message.setOnClickListener(v -> goToAudio(holder, note));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView character, message;
        private final ImageView edit, delete, noteIcon;
        private final EditText editMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            character = itemView.findViewById(R.id.character);
            message = itemView.findViewById(R.id.message);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            noteIcon = itemView.findViewById(R.id.note_icon);
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

    private void goToAudio(NoteAdapter.ViewHolder holder, Note note) {
        Storage.getInstance().saveCurrentTime(holder.itemView.getContext(), order, chapter, note.getAtTime());
        Context context = holder.itemView.getContext();
        Intent intent = new Intent(context, AudioActivity.class);
        intent.putExtra(Constants.BOOK_ORDER, order);
        intent.putExtra(Constants.CHAPTER, chapter);
        context.startActivity(intent);
    }
}
