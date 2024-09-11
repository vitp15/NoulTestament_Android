package com.example.noultestament.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.noultestament.R;
import com.example.noultestament.utils.AudioPlayer;
import com.example.noultestament.utils.Constants;
import com.example.noultestament.utils.Note;
import com.example.noultestament.utils.Storage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {
    private int chapter, order;
    private boolean fromForceClosed;
    private TextView bookName, chapterTxt, currentTime, duration, addNote;
    private SeekBar seekBar;
    private ImageView playPause, prev, next, replay, forward, back;
    private ConstraintLayout marks_layout;
    private AudioPlayer audioPlayer;
    private boolean isDragged = false;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        order = getIntent().getIntExtra(Constants.BOOK_ORDER, 0);
        chapter = getIntent().getIntExtra(Constants.CHAPTER, 0);
        fromForceClosed = getIntent().getBooleanExtra(Constants.FORCE_CLOSED, false);
        setupData();
        setupAudio(0);
        AudioActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!audioPlayer.isNull()) {
                    if (!isDragged) {
                        seekBar.setProgress(audioPlayer.getCurrentPosition());
                    }
                    currentTime.setText(AudioPlayer.convertTime(audioPlayer.getCurrentPosition()));
                }
                if (audioPlayer != null && audioPlayer.isPlaying()) {
                    playPause.setImageResource(R.drawable.pause);
                } else {
                    playPause.setImageResource(R.drawable.play);
                }
                new Handler().postDelayed(this, 100);
            }
        });
        back.setOnClickListener(v -> onBackPressed());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDragged = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isDragged = false;
                audioPlayer.seekTo(seekBar.getProgress());
            }
        });
        playPause.setOnClickListener(v -> {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pauseAudio();
            } else {
                audioPlayer.playAudio();
            }
        });
        prev.setOnClickListener(v -> setupAudio(2));
        next.setOnClickListener(v -> setupAudio(1));
        replay.setOnClickListener(v -> audioPlayer.replay(5));
        forward.setOnClickListener(v -> audioPlayer.forward(5));
        addNote.setOnClickListener(v -> {
            if (Storage.getInstance().existNoteAtTime(order, chapter, audioPlayer.getCurrentPosition(), 10)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("")
                        .setMessage(R.string.message_min_interval)
                        .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                        .setCancelable(true)
                        .show();
            } else if (!Storage.getInstance().hasLessNotes(order, chapter, 10)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("")
                        .setMessage(R.string.message_max_notes)
                        .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                        .setCancelable(true)
                        .show();
            } else {
                Storage.getInstance().saveCurrentTime(this, audioPlayer.getOrder(), audioPlayer.getCurrentChapter(), audioPlayer.getCurrentPosition());
                Intent intent = new Intent(this, NotesActivity.class);
                intent.putExtra(Constants.BOOK_ORDER, audioPlayer.getOrder());
                intent.putExtra(Constants.CHAPTER, audioPlayer.getCurrentChapter());
                intent.putExtra(Constants.ADD_NOTE, true);
                intent.putExtra(Constants.AT_TIME, audioPlayer.getCurrentPosition());
                startActivity(intent);
            }
        });
    }

    private void setupData() {
        bookName = findViewById(R.id.book_name);
        chapterTxt = findViewById(R.id.book_chapter);
        currentTime = findViewById(R.id.current_time);
        duration = findViewById(R.id.total_time);
        addNote = findViewById(R.id.addNote);
        seekBar = findViewById(R.id.track);
        playPause = findViewById(R.id.play_pause);
        prev = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        replay = findViewById(R.id.replay);
        forward = findViewById(R.id.forward);
        back = findViewById(R.id.back);
        marks_layout = findViewById(R.id.marks_layout);
    }

    private void setupAudio(int option) {
        try {
            switch (option) {
                case 1:
                    audioPlayer.next();
                    break;
                case 2:
                    audioPlayer.previous();
                    break;
                case 0:
                default:
                    audioPlayer = new AudioPlayer(this, chapter, order);
                    audioPlayer.setupAudio();
                    break;
            }
            bookName.setText(Storage.getInstance().getBook(audioPlayer.getOrder()).getName());
            chapterTxt.setText(String.valueOf(audioPlayer.getCurrentChapter()));
            audioPlayer.setOnCompletionListener(mp -> setupAudio(1));
            if (!fromForceClosed) {
                audioPlayer.playAudio();
            }
            fromForceClosed = false;
        } catch (FileNotFoundException e) {
            bookName.setText(R.string.chapter_unaviable);
            chapterTxt.setText("");
        }
        seekBar.setProgress(audioPlayer.getCurrentPosition());
        seekBar.setMax(audioPlayer.getDuration());
        currentTime.setText(AudioPlayer.convertTime(audioPlayer.getCurrentPosition()));
        duration.setText(AudioPlayer.convertTime(audioPlayer.getDuration()));
    }

    @Override
    public void onBackPressed() {
        isBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Storage.getInstance().saveCurrentTime(this, audioPlayer.getOrder(), audioPlayer.getCurrentChapter(), audioPlayer.getCurrentPosition());
        if (!isBackPressed) {
            Storage.getInstance().saveForceClosed(this, audioPlayer.getOrder(), audioPlayer.getCurrentChapter());
        } else {
            audioPlayer.stopAudio();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Storage.getInstance().removeCurrentTime(this, audioPlayer.getOrder(), audioPlayer.getCurrentChapter());
        Storage.getInstance().removeForceClosed(this);
    }
}
