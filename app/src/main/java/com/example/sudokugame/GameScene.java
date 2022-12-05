package com.example.sudokugame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameScene extends AppCompatActivity {

    private int mInterval = 300;
    private Handler mHandler;
    int startTime;

    BoardView boardRef;
    ButtonsView buttonsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        boardRef = (BoardView) findViewById(R.id.boardView);
        buttonsRef = (ButtonsView) findViewById(R.id.buttonsView);

        if(buttonsRef != null)
            buttonsRef.setBoardViewRef(boardRef);

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupCanvas();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
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

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateTime();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    protected void startRepeatingTask() {
        startTime = (int) System.currentTimeMillis();
        mStatusChecker.run();
    }

    protected String msToString(int millis){
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        seconds %= 60;

        String zero = (seconds < 10? "0" : "");

        return ("" + minutes + ":" + zero + seconds);
    }

    protected void updateTime() {
        int currentTime = (int) System.currentTimeMillis() - startTime;
        TextView timeView = findViewById(R.id.timeView);
        timeView.setText(msToString(currentTime));
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}