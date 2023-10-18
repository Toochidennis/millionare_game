package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

class FailDialog extends Dialog {
    Context context;
    String amount;
    DBHelper dbHelper;
    public FailDialog(@NonNull Context context,String amount) {
        super(context);
        this.context=context;
        this.amount = amount;
        dbHelper = new DBHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fail_dialog_layout);
        TextView amountText = findViewById(R.id.amount_won);
        TextView descText = findViewById(R.id.desc);
        CardView menuBtn = findViewById(R.id.menu);
        CardView newBtn = findViewById(R.id.new_game);
        CardView bg = findViewById(R.id.rootview);
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language","en");
        int endcolor = sharedPreferences.getInt("end_color",0xFF6200EE);
        int startColor = sharedPreferences.getInt("start_color",0xFFBB86FC);        int cardBackground = sharedPreferences.getInt("card_background",0x03045e);
        bg.setCardBackgroundColor(endcolor);


        amountText.setText(amount);
        descText.setText("You won "+amount);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                String json = dbHelper.buildJson();
                Intent intent = new Intent(context,GameActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Json",json);
                context.startActivity(intent);
            }
        });

        menuBtn.setCardBackgroundColor(cardBackground);
        newBtn.setCardBackgroundColor(cardBackground);
    }
}
