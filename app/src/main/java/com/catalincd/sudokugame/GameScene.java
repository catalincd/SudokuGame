package com.catalincd.sudokugame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class GameScene extends AppCompatActivity {

    private int mInterval = 200;
    private Handler mHandler;
    int startTime;
    int diff;
    boolean destroyed = false;

    BoardView boardRef;
    ButtonsView buttonsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        diff = Integer.parseInt(getIntent().getStringExtra("DIFFICULTY"));
        System.out.println("DIFF: " + diff);

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

    public String boardToString(int[][] board){
        String boardStr = "";
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                boardStr = boardStr + board[i][j];
            }
        }

        return boardStr;
    }

    public String intToDiff(int diffInt){
        String[] difficulties = {"SIMPLE", "EASY", "MEDIUM", "HARD", "EXTREME"};

        return difficulties[(diffInt - 5) / 10];
    }

    protected void updateTime() {
        int currentTime = (int) System.currentTimeMillis() - startTime;
        TextView timeView = findViewById(R.id.timeView);
        timeView.setText(msToString(currentTime));

        TextView progressView = findViewById(R.id.progressView);
        progressView.setText(boardRef.getProgress());

        if(boardRef.getProgressInt() == 0 && !destroyed){

            Intent intent = new Intent(GameScene.this, EndGame.class);
            String board = boardToString(boardRef.getFinalBoard());
            intent.putExtra("FINAL_BOARD", board);
            intent.putExtra("TIME", msToString(currentTime));
            intent.putExtra("MOVES", boardRef.getTotalMoves());
            intent.putExtra("DIFFICULTY", intToDiff(diff));
            startActivity(intent);
            finish();

            destroyed = true;
        }
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}