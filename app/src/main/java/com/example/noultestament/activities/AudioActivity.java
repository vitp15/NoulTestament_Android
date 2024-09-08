package com.example.noultestament.activities;

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
import com.example.noultestament.utils.Storage;

import java.io.FileNotFoundException;

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
