package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> startActivity(new Intent(PlayActivity.this, HelpActivity.class)));


        final Button button = findViewById(R.id.playButton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, DiceActivity.class);
            startActivity(intent);
            finish();
        });
    }

    }


