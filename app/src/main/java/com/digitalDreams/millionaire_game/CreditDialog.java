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
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        TextView facebookBtn = findViewById(R.id.facebook_link);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);

                    String facebookScheme = "fb://page/" + facebookId;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getContext().startActivity(intent);
                    Log.i("response","page "+facebookScheme);

                }
                catch(Exception e) {
                    e.printStackTrace();
                    // Cache and Open a url in browser
                    String facebookProfileUri = "https://www.facebook.com/" + facebookUrl;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUri));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getContext().startActivity(intent);

                }

            }
        });

        TextView emailBtn = findViewById(R.id.email_link);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",emailAddress, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContext().startActivity(Intent.createChooser(intent, "Send Us a mail"));
            }
        });

        TextView callBtn = findViewById(R.id.phone_link);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContext().startActivity(intent);
            }
        });

        TextView twitterBtn = findViewById(R.id.twitter_link);
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id="+twitterLink));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitterLink));
                }
                getContext().startActivity(intent);
            }
        });
    }
}
