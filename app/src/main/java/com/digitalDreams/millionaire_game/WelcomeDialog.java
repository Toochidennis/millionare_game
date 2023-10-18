package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class WelcomeDialog extends Dialog {
    LinearLayout background;
    Context context;


    public WelcomeDialog(@NonNull Context context) {

        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_dialog);
        ImageView closeBtn = findViewById(R.id.close);

//        background = findViewById(R.id.background);
//        container = findViewById(R.id.container);
//        layoutInflater = LayoutInflater.from(context);
       // background.getBackground().setAlpha(45);




        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



//        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
//        String languageCode = sharedPreferences.getString("language","en");
//        int endcolor = sharedPreferences.getInt("end_color",context.getResources().getColor(R.color.purple_dark));
//        int startColor = sharedPreferences.getInt("start_color",context.getResources().getColor(R.color.purple_500));
//
//        GradientDrawable gd = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM,
//                new int[] {startColor,endcolor});


       // inflateOptions();
        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

//    private void inflateOptions(){
//        container.removeAllViews();
//
//        for (int i = 0; i < 4; i++){
//            View view = layoutInflater.inflate(R.layout.options_layout,container,false);
//            String[] alpha = {"A","B","C","D"};
//            TextView opt = view.findViewById(R.id.opt);
//            TextView alpha_opt = view.findViewById(R.id.alpha_opt);
//            alpha_opt.setText(alpha[i]);
//            opt.setText(String.valueOf(i));
//            container.addView(view);
//
//        }
//
//
//    }
}
