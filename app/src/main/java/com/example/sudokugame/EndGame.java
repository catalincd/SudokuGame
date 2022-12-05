package com.example.sudokugame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EndGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }



        BoardView boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(stringToBoard(getIntent().getStringExtra("FINAL_BOARD")));

        setButtons();
    }

    public int[][] stringToBoard(String boardStr){
        int[][] board = new int[9][9];

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                int current = i * 9 + j;
                board[i][j] = Integer.parseInt(String.valueOf(boardStr.charAt(current)));
            }
        }

        return board;
    }

    protected void setButtons() {
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EndGame.this, MainActivity.class));
                finish();
            }
        });
    }
}