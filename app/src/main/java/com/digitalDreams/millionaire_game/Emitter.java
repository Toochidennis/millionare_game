package com.digitalDreams.millionaire_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Emitter extends AppCompatActivity {
    NewParticle newParticle;
    ImageView imageView;
    RelativeLayout imageRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emitter);
        imageView = findViewById(R.id.start);
        imageRoot = findViewById(R.id.imageroot);

        newParticle = new NewParticle(this, R.layout.single_star,imageRoot,100,100);




    }
}