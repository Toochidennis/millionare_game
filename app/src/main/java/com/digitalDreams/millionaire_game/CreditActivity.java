package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreditActivity extends AppCompatActivity {
    String facebookId = "";
    String emailAddress = "info@digitaldreamsng.com";
    String phoneNumber = "09064660137";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        TextView facebookBtn = findViewById(R.id.facebook_link);
        facebookBtn.setText(facebookId);
        facebookBtn.setOnClickListener(view -> {
            try {
                getPackageManager().getPackageInfo("com.facebook.katana", 0);

                String facebookScheme = "fb://profile/" + facebookId;
                new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
            }
            catch(Exception e) {

                // Cache and Open a url in browser
                String facebookProfileUri = "https://www.facebook.com/" + facebookId;
                 new Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUri));
            }

        });

        TextView emailBtn = findViewById(R.id.email_link);
        emailBtn.setText(emailAddress);
        emailBtn.setOnClickListener(view -> {
            Toast.makeText(CreditActivity.this,"clicked",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",emailAddress, null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, "Send Us a mail"));
        });

        TextView callBtn = findViewById(R.id.phone_link);
        callBtn.setText(phoneNumber);
        callBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        });
    }
}