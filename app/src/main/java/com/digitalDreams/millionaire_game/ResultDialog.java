package com.digitalDreams.millionaire_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ResultDialog extends Dialog {
    Context context;
    String dailyPsition;
    String weeklyPosition;
    String txt;

    public ResultDialog(@NonNull Context context, String dailyPsition, String weeklyPosition, String txt) {


        super(context);
        this.dailyPsition = dailyPsition;
        this.weeklyPosition = weeklyPosition;
        this.context = context;
        this.txt = txt;
    }

    LinearLayout background;
    TextView dayPosition;
    TextView weekPosition;

    TextView daily;
    TextView weekly;
    LinearLayout mainBg;
    TextView txtField;
    TextView txtField2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_dialog);

        background = findViewById(R.id.background);

        ImageView closeBtn = findViewById(R.id.close);
        RelativeLayout view_ranking = findViewById(R.id.view_ranking);
        RelativeLayout view_history = findViewById(R.id.view_history);
        RelativeLayout play_again = findViewById(R.id.play_again);
        txtField = findViewById(R.id.txt);
        txtField2 = findViewById(R.id.txt2);


        closeBtn.setOnClickListener(view -> {
            Utils.darkBlueBlink(closeBtn, context);

            dismiss();
        });

        view_history.setOnClickListener(view -> {
            Intent i = new Intent(context, HistoryDetals.class);
            Utils.darkBlueBlink(view_history, context);
            i.putExtra("date_played", Utils.lastDatePlayed);

            context.startActivity(i);
        });

        play_again.setOnClickListener(view -> {
            Utils.greenBlink(play_again, context);
            Intent intent = new Intent(context, GameActivity2.class);
            context.startActivity(intent);
            dismiss();

            PlayDetailsActivity.playerDetailsActivity.finish();
        });

        view_ranking.setOnClickListener(view -> {
            Utils.darkBlueBlink(view_ranking, context);

            Utils.destination_activity = LeaderBoard.class;
            Intent i = new Intent(context, LeaderBoard.class);

            context.startActivity(i);

        });


        txtField = findViewById(R.id.txt);
        mainBg = findViewById(R.id.main);
        mainBg.setBackgroundColor(context.getResources().getColor(R.color.black));

        dayPosition = findViewById(R.id.position);
        weekPosition = findViewById(R.id.position2);


        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);

        String[] splitedText = txt.split("-");

        txtField.setText(splitedText[0]);
        txtField2.setText(splitedText[1]);

        daily.setText(position(String.valueOf(Integer.parseInt(dailyPsition) + 1), 1));
        weekly.setText(position(String.valueOf(Integer.parseInt(weeklyPosition) + 1), 2));


        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        int endcolor = sharedPreferences.getInt("end_color", context.getResources().getColor(R.color.purple_dark));
        int startColor = sharedPreferences.getInt("start_color", context.getResources().getColor(R.color.purple_500));

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{startColor, endcolor});

        background.setBackgroundDrawable(gd);
    }


    public String position(String position, int type) {
//        String weeklyText1 = weeklyTxt.getText().toString();
//        String dailyTxt1 = dailyTxt.getText().toString();
        if (position.endsWith("11") || position.endsWith("12") || position.endsWith("13")) {
            if (type == 1) {

                dayPosition.setText("th ");


                return position;
            }
            weekPosition.setText("th ");
            return position;

        } else if (position.endsWith("1")) {
            if (type == 1) {

                dayPosition.setText("'st ");


                return position;
            }
            weekPosition.setText("'st ");
            return position;

        } else if (position.endsWith("2")) {
            if (type == 1) {
                dayPosition.setText("nd ");
                return position;
            }
            weekPosition.setText("nd ");
            return position;

        } else if (position.endsWith("3")) {

            if (type == 1) {
                dayPosition.setText("rd ");
                return position;
            }
            weekPosition.setText("rd ");
            return position;

        } else {

            if (type == 1) {
                dayPosition.setText("th ");
                return position;
            }
            weekPosition.setText("th ");
            return position;

        }

    }

}
