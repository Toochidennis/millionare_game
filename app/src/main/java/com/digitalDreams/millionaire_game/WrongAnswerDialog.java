package com.digitalDreams.millionaire_game;

import static android.content.Context.MODE_PRIVATE;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.playBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.AudioManager.stopBackgroundMusic;
import static com.digitalDreams.millionaire_game.alpha.Constants.APPLICATION_DATA;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_CONTINUE_GAME;
import static com.digitalDreams.millionaire_game.alpha.Constants.SHOULD_REFRESH_QUESTION;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.digitalDreams.millionaire_game.alpha.AudioManager;
import com.digitalDreams.millionaire_game.alpha.ExplanationBottomSheetDialog;
import com.digitalDreams.millionaire_game.alpha.testing.database.Question;
import com.digitalDreams.millionaire_game.alpha.models.QuestionModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;


public class WrongAnswerDialog extends Dialog {
    Context context;
    RewardedAd mRewardedVideoAd;

    QuestionModel questionModel;
    Question question;

    private SharedPreferences sharedPreferences;


    public WrongAnswerDialog(@NonNull Context context, QuestionModel questionModel) {
        super(context);
        this.context = context;

        this.questionModel = questionModel;
        AdManager.loadInterstitialAd((Activity) context);
        AdManager.loadRewardedAd((Activity) context);

    }

    public WrongAnswerDialog(@NonNull Context context, Question question) {
        super(context);
        this.context = context;

        this.question = question;
        AdManager.loadInterstitialAd((Activity) context);
        AdManager.loadRewardedAd((Activity) context);
    }


    LinearLayout continueButton, closeButton, giveUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_answer_layout);

        continueButton = findViewById(R.id.continue_game);
        giveUpButton = findViewById(R.id.give_up);
        closeButton = findViewById(R.id.exit_btn);

        setRootViewBackgroundColor();
        handleViewClicks();
    }

    private void setRootViewBackgroundColor() {
        sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        int endColor = sharedPreferences.getInt("end_color", context.getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", context.getResources().getColor(R.color.purple_500));

        RelativeLayout relativeLayout = findViewById(R.id.rootView);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        relativeLayout.setBackground(gradientDrawable);
    }


    private void handleViewClicks() {
        continueButton.setOnClickListener(continue_btn ->
                showRewardedAdWithListener()
        );

        giveUpButton.setOnClickListener(giveUpButton -> {
            showExplanationDialog();
        });

        closeButton.setOnClickListener(view -> {
            showExplanationDialog();
        });
    }

    private void showExplanationDialog() {
        dismiss();
        ExplanationBottomSheetDialog explanationBottomSheetDialog;

        if (questionModel == null) {
            explanationBottomSheetDialog = new ExplanationBottomSheetDialog(getContext(), question);
        } else {
            explanationBottomSheetDialog = new ExplanationBottomSheetDialog(getContext(), questionModel);
        }
        explanationBottomSheetDialog.show();

        explanationBottomSheetDialog.setOnDismissListener(dialog -> {
            dialog.dismiss();

            startFailureActivity();
        });
    }

    public void showRewardedAdWithListener() {
        AudioManager.greenBlink(context, continueButton);

        if (Utils.isOnline(context)) {
            AdManager.showRewardedAd((Activity) context);
            try {
                AdManager.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad.
                        // Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        updateSharedPreference();
                        playBackgroundMusic(getContext());
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when ad fails to show.
                        dismissDialogAndStartFailureActivity();
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        //Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        // Log.d(TAG, "Ad showed fullscreen content.");
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();

                AdManager.showInterstitial((Activity) context);
                if (AdManager.interstitialAd != null) {
                    AdManager.interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                        }
                    });
                } else {
                    dismissDialogAndStartFailureActivity();
                }
            }

            dismiss();
        } else {
            dismissDialogAndStartFailureActivity();
        }

    }

    private void dismissDialogAndStartFailureActivity() {
        dismiss();
        context.startActivity(new Intent(getContext(), FailureActivity.class));
    }

    private void startFailureActivity() {
        stopBackgroundMusic();
        context.startActivity(new Intent(getContext(), FailureActivity.class));
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APPLICATION_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOULD_CONTINUE_GAME, true);
        editor.putBoolean(SHOULD_REFRESH_QUESTION, false);
        editor.apply();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        AudioManager.releaseMusicResources();
    }
}
