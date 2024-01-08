package com.digitalDreams.millionaire_game;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class CreditDialog extends Dialog {
    String facebookUrl = "digitaldreamsng";
    String facebookId = "104648489003";
    String emailAddress = "info@digitaldreamsng.com";
    String phoneNumber = "09064660137";
    String twitterLink = "DigitalDreamsNG";

    public CreditDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_credit);

        ImageView closeBtn = findViewById(R.id.close);
        closeBtn.setOnClickListener(view -> dismiss());

        TextView facebookBtn = findViewById(R.id.facebook_link);

        facebookBtn.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/" + facebookUrl));
                getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                // Cache and Open a url in browser
                String facebookProfileUri = "https://www.facebook.com/" + facebookUrl;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUri));
                getContext().startActivity(intent);
            }
        });

        TextView emailBtn = findViewById(R.id.email_link);
        emailBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + emailAddress));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
            } else {
                getContext().startActivity(Intent.createChooser(intent, "Send Us a mail"));
            }
        });

        TextView callBtn = findViewById(R.id.phone_link);
        callBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
            }
        });

        TextView twitterBtn = findViewById(R.id.twitter_link);
        twitterBtn.setOnClickListener(view -> {
            Intent intent;
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + twitterLink));
                getContext().startActivity(intent);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitterLink));
                getContext().startActivity(intent);
            }
        });
    }
}
