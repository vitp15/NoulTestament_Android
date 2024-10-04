package com.lovib.noultestament.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioPlayer {
    private final Context context;
    private MediaPlayer mediaPlayer;
    private int currentChapter;
    private int order;

    public AudioPlayer(Context context, int currentChapter, int order) {
        this.context = context;
        this.currentChapter = currentChapter;
        this.order = order;
    }

    public void setupAudio() throws FileNotFoundException {
        String fileName = Storage.getInstance().getBook(order).getAudioName(currentChapter) + ".mp3";
        stopAudio();
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            assetFileDescriptor.close();
        } catch (IOException e) {
            throw new FileNotFoundException("File not found");
        }
        int time = Storage.getInstance().getCurrentTime(context, order, currentChapter);
        seekTo(time);
        Storage.getInstance().removeCurrentTime(context, order, currentChapter);
    }

    public boolean isNull() {
        return mediaPlayer == null;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public int getOrder() {
        return order;
    }

    public int getDuration() {
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pauseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void next() throws FileNotFoundException {
        if (currentChapter < Storage.getInstance().getBook(order).getChapters()) {
            currentChapter++;
            stopAudio();
            setupAudio();
            playAudio();
        } else if (order < Storage.getInstance().getBooks().size()) {
            order++;
            currentChapter = 1;
            stopAudio();
            setupAudio();
            playAudio();
        }
    }

    public void previous() throws FileNotFoundException {
        if (currentChapter > 1) {
            currentChapter--;
            stopAudio();
            setupAudio();
            playAudio();
        } else if (order > 1) {
            order--;
            currentChapter = Storage.getInstance().getBook(order).getChapters();
            stopAudio();
            setupAudio();
            playAudio();
        }
    }

    public void replay(int time) {
        if (mediaPlayer != null) {
            int position = mediaPlayer.getCurrentPosition();
            position -= time * 1000;
            if (position < 0) {
                position = 0;
            }
            mediaPlayer.seekTo(position);
        }
    }

    public void forward(int time) {
        if (mediaPlayer != null) {
            int position = mediaPlayer.getCurrentPosition();
            position += time * 1000;
            if (position > mediaPlayer.getDuration()) {
                position = mediaPlayer.getDuration();
            }
            mediaPlayer.seekTo(position);
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(listener);
        }
    }

    public static String convertTime(int time) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
    }
}
