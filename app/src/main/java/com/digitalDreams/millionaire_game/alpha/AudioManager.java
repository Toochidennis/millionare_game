package com.digitalDreams.millionaire_game.alpha;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import com.digitalDreams.millionaire_game.R;

public class AudioManager {

    private static MediaPlayer backgroundMediaPlayer;
    private static MediaPlayer successMediaPlayer;
    private static MediaPlayer failureMediaPlayer;
    private static MediaPlayer winningMediaPlayer;
    private static MediaPlayer darkBlueMediaPlayer;
    private static MediaPlayer greenMediaPlayer;

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

    @SuppressLint("ClickableViewAccessibility")
    public static void greenBlink(Context context, View view) {
        try {
            if (greenMediaPlayer == null) {
                 greenMediaPlayer=  MediaPlayer.create(context, R.raw.play);
            }
            greenMediaPlayer.start();

            view.setOnTouchListener((v, event)->{
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    view.setBackgroundResource(R.drawable.dark_play);
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    view.setBackgroundResource(R.drawable.playbtn_bg);
                }

                return false;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void darkBlueBlink(Context context, View view) {
        try {
            if (darkBlueMediaPlayer == null) {
                darkBlueMediaPlayer = MediaPlayer.create(context, R.raw.others);
            }
            darkBlueMediaPlayer.start();

            view.setOnTouchListener((v, event)->{
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    view.setBackgroundResource(R.drawable.ic_hex_2);
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    view.setBackgroundResource(R.drawable.ic_hexnow);
                }

                return false;
            });

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

            if (greenMediaPlayer != null) {
                greenMediaPlayer.release();
                greenMediaPlayer = null;
            }

            if (darkBlueMediaPlayer != null) {
                darkBlueMediaPlayer.release();
                darkBlueMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
