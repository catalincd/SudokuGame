package com.catalincd.sudokugame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DifficultySelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selector);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setButtons();
    }

    protected void setButtons() {
        Button simpleDiff = (Button) findViewById(R.id.simpleDiff);
        simpleDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoard(5);
            }
        });

        Button easyDiff = (Button) findViewById(R.id.easyDiff);
        easyDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoard(15);
            }
        });

        Button mediumDiff = (Button) findViewById(R.id.mediumDiff);
        mediumDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoard(25);
            }
        });

        Button hardDiff = (Button) findViewById(R.id.hardDiff);
        hardDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoard(35);
            }
        });

        Button extremeDiff = (Button) findViewById(R.id.extremeDiff);
        extremeDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoard(45);
            }
        });

        Button homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startBoard(int diff){
        Intent intent = new Intent(DifficultySelector.this, GameScene.class);
        intent.putExtra("DIFFICULTY", "" + diff);
        SudokuGenerator.DIFFICULTY = diff;
        startActivity(intent);
        finish();
    }
}