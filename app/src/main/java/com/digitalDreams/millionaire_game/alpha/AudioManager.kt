package com.digitalDreams.millionaire_game.alpha

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import com.digitalDreams.millionaire_game.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AudioManager {

    private var backgroundMediaPlayer: MediaPlayer? = null
    private var successMediaPlayer: MediaPlayer? = null
    private var failureMediaPlayer: MediaPlayer? = null
    private var winningMediaPlayer: MediaPlayer? = null
    private var darkBlueMediaPlayer: MediaPlayer? = null
    private var greenMediaPlayer: MediaPlayer? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @JvmStatic
    fun playBackgroundMusic(context: Context) {
        coroutineScope.launch {
            try {
                if (backgroundMediaPlayer == null) {
                    backgroundMediaPlayer = MediaPlayer.create(context, R.raw.background_sound)
                    backgroundMediaPlayer?.isLooping = true
                    val audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                    backgroundMediaPlayer?.setAudioAttributes(audioAttributes)
                }

                if (backgroundMediaPlayer?.isPlaying == false) {
                    backgroundMediaPlayer?.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun pauseBackgroundMusic() {
        coroutineScope.launch {
            try {
                if (backgroundMediaPlayer?.isPlaying == true) {
                    backgroundMediaPlayer?.pause()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun stopBackgroundMusic() {
        coroutineScope.launch {
            try {
                if (backgroundMediaPlayer != null) {
                    backgroundMediaPlayer?.stop()
                    backgroundMediaPlayer?.release()
                    backgroundMediaPlayer = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun playSuccessSound(context: Context) {
        coroutineScope.launch {
            try {
                if (successMediaPlayer == null) {
                    successMediaPlayer = MediaPlayer.create(context, R.raw.success_sound)
                }
                successMediaPlayer!!.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun playFailureSound(context: Context) {
        coroutineScope.launch {
            try {
                if (failureMediaPlayer == null) {
                    failureMediaPlayer = MediaPlayer.create(context, R.raw.failure_sound2)
                }
                failureMediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun playWinningSound(context: Context) {
        coroutineScope.launch {
            try {
                if (winningMediaPlayer == null) {
                    winningMediaPlayer = MediaPlayer.create(context, R.raw.winning_sound)
                }
                winningMediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @SuppressLint("ClickableViewAccessibility")
    fun greenBlink(context: Context, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (greenMediaPlayer == null) {
                    greenMediaPlayer = MediaPlayer.create(context, R.raw.play)
                }
                greenMediaPlayer?.start()
                view.setOnTouchListener { _: View?, event: MotionEvent ->
                    if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                        view.setBackgroundResource(R.drawable.dark_play)
                    }
                    if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                        view.setBackgroundResource(R.drawable.playbtn_bg)
                    }
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    @JvmStatic
    @SuppressLint("ClickableViewAccessibility")
    fun darkBlueBlink(context: Context, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (darkBlueMediaPlayer == null) {
                    darkBlueMediaPlayer = MediaPlayer.create(context, R.raw.others)
                }
                darkBlueMediaPlayer!!.start()
                view.setOnTouchListener { v: View?, event: MotionEvent ->
                    if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                        view.setBackgroundResource(R.drawable.ic_hex_2)
                    }
                    if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                        view.setBackgroundResource(R.drawable.ic_hexnow)
                    }
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun releaseMusicResources() {
        coroutineScope.launch {
            try {
                backgroundMediaPlayer?.release()
                successMediaPlayer?.release()
                failureMediaPlayer?.release()
                winningMediaPlayer?.release()
                greenMediaPlayer?.release()
                darkBlueMediaPlayer?.release()


                darkBlueMediaPlayer = null
                greenMediaPlayer = null
                winningMediaPlayer = null
                failureMediaPlayer = null
                successMediaPlayer = null
                backgroundMediaPlayer = null

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
