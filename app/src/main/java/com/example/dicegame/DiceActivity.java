package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class DiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Timer timer;
    Integer h_dice1, h_dice2, a_dice1, a_dice2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        setSpinnerValues();
        timer = new Timer();
        this.triggerDiceStep();
        rollDiceButtonListener();
    }

    private void setSpinnerValues() {
        //Setting up Spinner Values.
        String[] diceValues = { "0", "1", "2", "3", "4", "5", "6"};
        Spinner spin = findViewById(R.id.row33_sp);
        spin.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter<CharSequence> ad  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diceValues);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.setAdapter(ad);
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
    {
        h_dice1 = position;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (position > 0) {
                        synchronized (this) {
                            initializeStep(R.id.notificationBar, R.string.notification_bar_h2_message,
                                    R.id.row34, -1, -1, -1, Boolean.FALSE);
                            setComputedScore(R.id.humanScore1, h_dice1, 0);

                        }
                    }
                    else{
                        DiceActivity.this.disableView(R.id.row34);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }, 0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        h_dice1 = 0;
    }

    private void triggerDiceStep() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    synchronized (this) {
                        a_dice1 = getRandom();
                        initializeStep(R.id.notificationBar, R.string.notification_bar_ai1_message, R.id.row31,
                                R.id.row31_pb, R.id.row31_tv2, a_dice1, Boolean.TRUE);
                        setComputedScore(R.id.aiScore, a_dice1, 0);
                        a_dice2 = getRandom();
                        initializeStep(R.id.notificationBar, R.string.notification_bar_ai2_message, R.id.row32,
                                R.id.row32_pb, R.id.row32_tv2, a_dice2, Boolean.TRUE);
                        setComputedScore(R.id.aiScore, a_dice1, a_dice2);
                        initializeStep(R.id.notificationBar, R.string.notification_bar_h1_message, R.id.row33,
                                -1, -1, -1, Boolean.FALSE);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }, 0);
    }

    private void setTextViewValue(Integer viewID, String viewMsg){
        this.runOnUiThread(() -> {
            TextView scoreView = findViewById(viewID);
            scoreView.setText(viewMsg);
        });
    }

    private Integer getComputedScore(Integer score1, Integer score2){
       return (score1 + score2) % 6;
    }
    private void setComputedScore(Integer scoreViewID, Integer score1, Integer score2){
        this.setTextViewValue(scoreViewID, String.valueOf(getComputedScore(score1, score2)));
    }

    private void initializeStep(Integer nb, Integer nb_msg, Integer ll_id, Integer pb_id,
                           Integer tv_id, Integer dice_val, Boolean enable_wait)
            throws InterruptedException {
        if(nb>-1) {
            this.setTextViewValue(nb, getString(nb_msg));
            this.enableView(ll_id);
        }
        if(enable_wait==Boolean.TRUE) {
            Thread.sleep(3000);
        }
        if(pb_id>-1) {
            this.runOnUiThread(() -> {
                DiceActivity.this.disableView(pb_id);
                setTextViewValue(tv_id, String.valueOf(dice_val));
            });
        }
    }

    private void modifyViewVisibility(Integer view_id, Integer visibility_status){
        this.runOnUiThread(() -> {
            View pb1 = findViewById(view_id);
            pb1.setVisibility(visibility_status);
        });
    }

    private void enableView(Integer view_id){
        this.modifyViewVisibility(view_id, View.VISIBLE);
    }

    private void disableView(Integer view_id){
        this.modifyViewVisibility(view_id, View.INVISIBLE);
    }

    private void removeView(Integer view_id) {
        this.modifyViewVisibility(view_id, View.GONE);
    }

    private Integer getRandom(){
        return 1;
    }

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
        intent.putExtra("resultTitle", resultTitleMessage);
        String resultDescriptionMessage = getString(resultDescription);
        intent.putExtra("resultDescription", resultDescriptionMessage);
        startActivity(intent);
        finish();
    }

    public void rollDiceButtonListener() {
        Button button = findViewById(R.id.row34_bt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                h_dice2 = getRandom();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        try {
                            synchronized (this) {
                                DiceActivity.this.removeView(R.id.row34_bt);
                                DiceActivity.this.enableView(R.id.row34_tv1);
                                h_dice2 = getRandom();
                                setTextViewValue(R.id.row34_tv2, String.valueOf(h_dice2));
                                setComputedScore(R.id.humanScore1, h_dice1, h_dice2);
                                setTextViewValue(R.id.notificationBar,
                                        getString(R.string.notification_bar_result_message));
                                Thread.sleep(1000);
                                computeResult();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, 0);
            }
        });
    }

    private void computeResult() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Integer ai_score = getComputedScore(a_dice1, a_dice2);
                Integer human_score = getComputedScore(h_dice1, h_dice2);
                if(human_score > ai_score)
                    DiceActivity.this.onGameWin();
                else if(human_score.equals(ai_score))
                    DiceActivity.this.onGameTie();
                else
                    DiceActivity.this.onGameLose();
            }
        }, 0);
    }

}