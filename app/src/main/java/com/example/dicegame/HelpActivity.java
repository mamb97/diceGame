package com.example.dicegame;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class HelpActivity extends AppCompatActivity {

    TextView HelpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        HelpText = findViewById(R.id.helpText);

        String string = "";
        try {
            InputStream inputStream = getAssets().open("HELP.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            if (inputStream.read(buffer) != -1){
                string = new String(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelpText.setText(string);
    }
}
