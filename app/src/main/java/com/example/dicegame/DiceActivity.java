package com.example.dicegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DiceActivity extends AppCompatActivity {

    Random random = new Random();
    Timer timer;
    Integer h_dice1, h_dice2, a_dice1, a_dice2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        timer = new Timer();
        this.triggerDiceStep();
        this.gridViewListener();
        this.rollDiceButtonListener();
    }

    // This method triggers the first two AI dice rolls and computes the ai score. Finally enables the Grid View for first human selection.
    private void triggerDiceStep() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            try {
                synchronized (this) {
                    a_dice1 = getRandom();
                    initializeStep(R.id.notificationBar, R.string.notification_bar_ai1_message, R.id.row32,
                            R.id.row32_pb, Boolean.TRUE);
                    rotateDice(a_dice1, R.id.row32_dice_image);
                    setComputedScore(R.id.aiScore, a_dice1, 0);
                    a_dice2 = getRandom();
                    initializeStep(R.id.notificationBar, R.string.notification_bar_ai2_message, R.id.row33,
                            R.id.row33_pb, Boolean.TRUE);
                    rotateDice(a_dice2,  R.id.row33_dice_image);
                    setComputedScore(R.id.aiScore, a_dice1, a_dice2);
                    initializeStep(R.id.notificationBar, R.string.notification_bar_h1_message, R.id.diceGridLayout,
                                -1, Boolean.FALSE);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            }


        }, 0);
    }

    // Displays the dice images for human selection. On click of any image, the grid disappears and enables the next button view.
    private void gridViewListener() {
        GridLayout grid = findViewById(R.id.diceGridLayout);
        ImageView row52_dice_image = findViewById(R.id.row52_dice_image);
        for (int i= 0; i < 6; i++){
            ImageView container = (ImageView) grid.getChildAt(i);
            int finalI = i;
            container.setOnClickListener(view -> {
                removeView(R.id.diceGridLayout);
                h_dice1 = finalI + 1;
                row52_dice_image.setImageResource(getDiceImageId(h_dice1));
                setComputedScore(R.id.humanScore1, h_dice1, 0);
                try {
                    initializeStep(R.id.notificationBar, R.string.notification_bar_h2_message, R.id.row5,
                            -1, Boolean.FALSE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Button listener to invoke the last dice roll, and compute results.
    public void rollDiceButtonListener() {
        ImageButton row53_bt = findViewById(R.id.row53_bt);
        row53_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        try {
                            synchronized (this) {
                                h_dice2 = getRandom();
                                DiceActivity.this.removeView(R.id.row53_bt);
                                rotateDice(h_dice2, R.id.row53_dice_image);
                                DiceActivity.this.enableView(R.id.row53_dice_image);
                                setComputedScore(R.id.humanScore1, h_dice1, h_dice2);
                                setTextViewValue(R.id.notificationBar,
                                        getString(R.string.notification_bar_result_message));
                                Thread.sleep(3000);
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

    //Utility method to return the diceImage based on the input number
    private Integer getDiceImageId(Integer number){
        switch(number) {
            case 1:
                return R.drawable.dice1v2;
            case 2:
                return R.drawable.dice2v2;
            case 3:
                return R.drawable.dice3v2;
            case 4:
                return R.drawable.dice4v2;
            case 5:
                return R.drawable.dice5v2;
            case 6:
                return R.drawable.dice6v2;
        }
        return -1;
    }

    // This method aptly sets the dice phase for a given number at a provided view
    private void rotateDice(Integer i, Integer viewId) {
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.rotate);
        ImageView dice_image1 = findViewById(viewId);
        dice_image1.startAnimation(anim);
        dice_image1.setImageResource(getDiceImageId(i));
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

    // This is a utility method to update notification bar messages and trigger sleep between operations if the enable_wait flag is True.
    private void initializeStep(Integer nb, Integer nb_msg, Integer ll_id, Integer pb_id,
                                Boolean enable_wait)
            throws InterruptedException {
        if(nb>-1) {
            this.setTextViewValue(nb, getString(nb_msg));
            this.enableView(ll_id);
        }
        if(enable_wait==Boolean.TRUE) {
            Thread.sleep(3000);
        }
        if(pb_id>-1) {
            this.runOnUiThread(() -> DiceActivity.this.disableView(pb_id));
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
        return random.nextInt(5)+1;
    }

    private void onGameWin(Integer ai_score, Integer human_score){
        Integer title = R.string.game_win_title;
        Integer desc = R.string.game_win_description;
        this.displayResult(title, desc, ai_score, human_score);
    }
    private void onGameLose(Integer ai_score, Integer human_score){
        Integer title = R.string.game_not_win_title;
        Integer desc = R.string.game_lost_description;
        this.displayResult(title, desc, ai_score, human_score);
    }
    private void onGameTie(Integer ai_score, Integer human_score){
        Integer title = R.string.game_not_win_title;
        Integer desc = R.string.game_tie_description;
        this.displayResult(title, desc, ai_score, human_score);
    }

    // Utility method to start the next intent to display the final results to the user.
    private void displayResult(Integer resultTitle, Integer resultDescription,
                               Integer ai_score, Integer human_score){
        Intent intent = new Intent(this, ResultActivity.class);
        String resultTitleMessage = getString(resultTitle);
        intent.putExtra("resultTitle", resultTitleMessage);
        String resultDescriptionMessage = getString(resultDescription);
        intent.putExtra("resultDescription", resultDescriptionMessage);
        intent.putExtra("aiScore", ai_score);
        intent.putExtra("humanScore", human_score);
        startActivity(intent);
        finish();
    }

    // This method is triggered after the rolldice method. It computes the results of the both the players to identify the game status.
    private void computeResult() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Integer ai_score = getComputedScore(a_dice1, a_dice2);
                Integer human_score = getComputedScore(h_dice1, h_dice2);
                if(human_score > ai_score)
                    DiceActivity.this.onGameWin(ai_score, human_score);
                else if(human_score.equals(ai_score))
                    DiceActivity.this.onGameTie(ai_score, human_score);
                else
                    DiceActivity.this.onGameLose(ai_score, human_score);
            }
        }, 0);
    }

}