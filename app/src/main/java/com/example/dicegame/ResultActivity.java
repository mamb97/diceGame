package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        displayTextResult();
        resetButtonListener();
        exitButtonListener();
    }

    private void exitButtonListener() {
        final Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
    }

    private void resetButtonListener() {
        final Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void displayTextResult() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        TextView resultTitleTextView = findViewById(R.id.resultTitle);
        resultTitleTextView.setText(extras.getString("resultTitle"));
        TextView resultDescTextView = findViewById(R.id.resultDescription);
        resultDescTextView.setText(extras.getString("resultDescription"));
    }
}