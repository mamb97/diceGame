package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    Button btnReadingText;
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        Button helpbutton = (Button) findViewById(R.id.helpButton);


        helpbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                startActivity(new Intent(getApplicationContext(), Help.class));
            }
        });


        final Button button = findViewById(R.id.playButton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, DiceActivity.class);
            startActivity(intent);
            finish();
        });
    }

    }


