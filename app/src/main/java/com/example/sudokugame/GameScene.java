package com.example.sudokugame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameScene extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        BoardView board = (BoardView) findViewById(R.id.boardView);
        ButtonsView theseButtons = (ButtonsView) findViewById(R.id.buttonsView);

        if(board == null)
            System.out.println("COXBOARD2");

        if(theseButtons != null)
            theseButtons.setBoardViewRef(board);
        else
            System.out.println("COX2");
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupCanvas();
    }

    protected void setupCanvas() {
        BoardView board = (BoardView) findViewById(R.id.boardView);
        ButtonsView theseButtons = (ButtonsView) findViewById(R.id.buttonsView);

        if(board == null)
            System.out.println("COXBOARD");

        if(theseButtons != null)
            theseButtons.setBoardViewRef(board);
        else
            System.out.println("COX");
        //board.setBoard(SudokuGenerator.generateBoard());
    }
}