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
        try {
            if (backgroundMediaPlayer == null) {
                backgroundMediaPlayer = MediaPlayer.create(context, R.raw.background_sound);
                backgroundMediaPlayer.setLooping(true);
                backgroundMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            }

            if (!backgroundMediaPlayer.isPlaying()) {
                backgroundMediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pauseBackgroundMusic() {
        try {
            if (backgroundMediaPlayer != null && backgroundMediaPlayer.isPlaying()) {
                backgroundMediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {
        try {
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.stop();
                backgroundMediaPlayer.release();
                backgroundMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSuccessSound(Context context) {
        try {
            if (successMediaPlayer == null) {
                successMediaPlayer = MediaPlayer.create(context, R.raw.success_sound);
            }
            successMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playFailureSound(Context context) {
        try {
            if (failureMediaPlayer == null) {
                failureMediaPlayer = MediaPlayer.create(context, R.raw.failure_sound2);
            }
            failureMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playWinningSound(Context context) {
        try {
            if (winningMediaPlayer == null) {
                winningMediaPlayer = MediaPlayer.create(context, R.raw.winning_sound);
            }
            winningMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void releaseMusicResources() {
        try {
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
            if (winningMediaPlayer != null) {
                winningMediaPlayer.release();
                winningMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
