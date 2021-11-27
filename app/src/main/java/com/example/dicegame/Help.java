package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class Help extends AppCompatActivity {

    TextView HelpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        HelpText = (TextView) findViewById(R.id.helpText);

        String string = "";
        try {
            InputStream inputStream = getAssets().open("HELP.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            string = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelpText.setText(string);
    }
}