package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

class ExitDialog extends Dialog {
    Activity context;

    public ExitDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog_layout);
        RelativeLayout yesBtn = findViewById(R.id.yes);
        RelativeLayout noBtn = findViewById(R.id.no);
        RelativeLayout rootView = findViewById(R.id.rootview);

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        int endColor = sharedPreferences.getInt("end_color", context.getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", context.getResources().getColor(R.color.purple_500));

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endColor});

        rootView.setBackground(gradientDrawable);

        relativeLayout = findViewById(R.id.close_dialog);

        relativeLayout.setOnClickListener(view -> dismiss());

        yesBtn.setOnClickListener(view -> {
            dismiss();
            context.finish();
        });

        noBtn.setOnClickListener(view -> {
            try {
                if (Utils.isOnline(context)) {
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.digitalDreams.millionaire_game")));
                } else {
                    context.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismiss();
        });
    }
}
