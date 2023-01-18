package com.catalincd.sudokugame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

        String diff = getIntent().getStringExtra("DIFFICULTY");
        String moves = getIntent().getStringExtra("MOVES");
        String time = getIntent().getStringExtra("TIME");

        TextView diffText = (TextView) findViewById(R.id.textView5);
        TextView movesText = (TextView) findViewById(R.id.textView6);
        TextView timeText = (TextView) findViewById(R.id.textView7);

        diffText.setText(diff);
        movesText.setText(moves);
        timeText.setText(time);

        writeToStats(diff, moves, time);

        setButtons();
    }

    public void writeToStats(String diff, String moves, String time){
        Context context = this;
        String line = "" + diff + "," + moves + "," + time + "\n";
        String previous = readStats();
        String str = previous + line;
        try (FileOutputStream fos = context.openFileOutput("stats.txt", Context.MODE_PRIVATE)) {
            fos.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readStats() {
        Context context = this;
        FileInputStream fis = null;
        String finalStr = "";
        try{
            fis = context.openFileInput("stats.txt");
        } catch (IOException e) {
            return "";
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            return "";
        } finally {
            finalStr = stringBuilder.toString();
        }

        return finalStr;
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