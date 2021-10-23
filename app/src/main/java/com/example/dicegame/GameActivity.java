package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    Timer timer;

    private void onGameWin(){
        Integer title = R.string.game_win_title;
        Integer desc = R.string.game_win_description;
        this.displayResult(title, desc);
    }
    private void onGameLose(){
        Integer title = R.string.game_not_win_title;
        Integer desc = R.string.game_lost_description;
        this.displayResult(title, desc);
    }
    private void onGameTie(){
        Integer title = R.string.game_not_win_title;
        Integer desc = R.string.game_tie_description;
        this.displayResult(title, desc);
    }

    private void displayResult(Integer resultTitle, Integer resultDescription){
        Intent intent = new Intent(this, ResultActivity.class);
        String resultTitleMessage = getString(resultTitle);
        String resultDescriptionMessage = getString(resultDescription);
        intent.putExtra("resultTitle", resultTitleMessage);
        intent.putExtra("resultDescription", resultDescriptionMessage);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameActivity.this.onGameWin();
            }
        }, 10000);
    }



}