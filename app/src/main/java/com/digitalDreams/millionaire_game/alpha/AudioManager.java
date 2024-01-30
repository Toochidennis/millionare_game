package com.digitalDreams.millionaire_game.alpha;

import android.content.Context;
import android.media.MediaPlayer;

import com.digitalDreams.millionaire_game.R;

public class AudioManager {

    private static MediaPlayer backgroundMediaPlayer;
    private static MediaPlayer successMediaPlayer;
    private static MediaPlayer failureMediaPlayer;
    private static MediaPlayer winningMediaPlayer;

    public static void playBackgroundMusic(Context context) {
        if (backgroundMediaPlayer == null) {
            backgroundMediaPlayer = MediaPlayer.create(context, R.raw.background_sound);
            backgroundMediaPlayer.setLooping(true);
            backgroundMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
        }
        if (!backgroundMediaPlayer.isPlaying()) {
            backgroundMediaPlayer.start();
        }
    }

    public static void pauseBackgroundMusic() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.isPlaying()) {
            backgroundMediaPlayer.pause();
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
            backgroundMediaPlayer.release();
            backgroundMediaPlayer = null;
        }
    }


    public static void playSuccessSound(Context context) {
        if (successMediaPlayer == null) {
            successMediaPlayer = MediaPlayer.create(context, R.raw.success_sound);
        }
        successMediaPlayer.start();
    }

    public static void playFailureSound(Context context) {
        if (failureMediaPlayer == null) {
            failureMediaPlayer = MediaPlayer.create(context, R.raw.failure_sound2);
        }
        failureMediaPlayer.start();
    }

    public static void playWinningSound(Context context){
        if (winningMediaPlayer == null){
            winningMediaPlayer = MediaPlayer.create(context, R.raw.winning_sound);
        }
        winningMediaPlayer.start();
    }



    public static void releaseAll() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.release();
            backgroundMediaPlayer = null;
        }
        if (successMediaPlayer != null) {
            successMediaPlayer.release();
            successMediaPlayer = null;
        }
        if (failureMediaPlayer != null) {
            failureMediaPlayer.release();
            failureMediaPlayer = null;
        }

        if (winningMediaPlayer != null){
            winningMediaPlayer.release();
            winningMediaPlayer = null;
        }
    }
}
