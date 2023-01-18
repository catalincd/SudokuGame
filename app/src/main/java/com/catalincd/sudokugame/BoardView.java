package com.catalincd.sudokugame;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Random;

public class BoardView extends View {

    private final Paint backgroundBrush = new Paint();
    private final Paint thinLineBrush = new Paint();
    private final Paint thickLineBrush = new Paint();
    private final Paint textBrush = new Paint();
    private final Paint textBoldBrush = new Paint();
    private final Paint selectedBrush = new Paint();
    private final Paint superSelectedBrush = new Paint();
    private final Paint textFailedBrush = new Paint();

    private int boardBackground;
    private int thinLineColor;
    private int thickLineColor;
    private int textColor;
    private int textBoldColor;
    private int selectedColor;
    private int superSelectedColor;
    private int textFailedColor;

    int selectedX = -1;
    int selectedY = -1;
    int selectedNum = 1;

    int boardSize;
    int cellSize;
    int groupSize;

    int filled = 0;
    int target = 0;

    int[][] board = new int[9][9];
    int[][] fillBoard = new int[9][9];
    int[][] failBoard = new int[9][9];
    int[][] colors = new int[9][9];

    private boolean autoFill = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setEvents();

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                board[i][j] = -1;
                fillBoard[i][j] = -1;
                failBoard[i][j] = -1;
            }
        }

        boardBackground = Color.rgb(0.9f, 0.9f, 0.9f);
        thinLineColor = Color.rgb(0.0f, 0.0f, 0.0f);
        thickLineColor = Color.rgb(0.0f, 0.0f, 0.0f);
        textColor = Color.rgb(0.2f, 0.1f, 0.4f);
        textBoldColor = Color.rgb(0.0f, 0.0f, 0.0f);
        selectedColor = Color.rgb(0.7f, 0.8f, 1.0f);
        superSelectedColor = Color.rgb(0.5f, 0.7f, 1.0f);
        textFailedColor = Color.rgb(1.0f, 0.1f, 0.3f);

        backgroundBrush.setStyle(Paint.Style.FILL);
        backgroundBrush.setColor(boardBackground);
        backgroundBrush.setAntiAlias(true);

        selectedBrush.setStyle(Paint.Style.FILL);
        selectedBrush.setColor(selectedColor);
        selectedBrush.setAntiAlias(true);

        superSelectedBrush.setStyle(Paint.Style.FILL);
        superSelectedBrush.setColor(superSelectedColor);
        superSelectedBrush.setAntiAlias(true);

        thinLineBrush.setStyle(Paint.Style.FILL);
        thinLineBrush.setColor(thinLineColor);
        thinLineBrush.setStrokeWidth(5);
        thinLineBrush.setAntiAlias(true);

        thickLineBrush.setStyle(Paint.Style.FILL);
        thickLineBrush.setColor(thickLineColor);
        thickLineBrush.setStrokeWidth(10);
        thickLineBrush.setAntiAlias(true);

        textBrush.setStyle(Paint.Style.FILL);
        textBrush.setColor(textColor);
        textBrush.setTextSize(90);
        //textBrush.setFakeBoldText(true);
        textBrush.setAntiAlias(true);

        textBoldBrush.setStyle(Paint.Style.FILL);
        textBoldBrush.setColor(textBoldColor);
        textBoldBrush.setTextSize(90);
        textBoldBrush.setFakeBoldText(true);
        textBoldBrush.setAntiAlias(true);

        textFailedBrush.setStyle(Paint.Style.FILL);
        textFailedBrush.setColor(textFailedColor);
        textFailedBrush.setTextSize(90);
        textFailedBrush.setFakeBoldText(true);
        textFailedBrush.setAntiAlias(true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BoardView,
                0, 0);

        try {
            autoFill = a.getBoolean(R.styleable.BoardView_autoFill, true);
        } finally {
            a.recycle();
        }
        if(autoFill) {
            //GameScene gameScene = (GameScene) getActivity();
            //int diff = Integer.parseInt(((GameScene) getActivity()).getIntent().getStringExtra("DIFFICULTY"));
            //System.out.println("DIFF: " + diff);
            //System.out.println("HERE IT FILLS");
            setBoard(SudokuGenerator.generateBoard());
        }

        filled = 0;
        target = 81;

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j] != -1)
                    target--;
            }
        }

        //fillInitialBoard(35);
    }

    public void setBoard(int[][] refBoard){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                board[i][j] = refBoard[i][j];
            }
        }
    }

    public int[] getCompletion(){
        int[] completed = new int[10];
        for(int i=0;i<10;i++)
            completed[i] = 0;

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j] != -1)
                    completed[board[i][j] - 1]++;

                if(fillBoard[i][j] != -1)
                    completed[fillBoard[i][j] - 1]++;
            }
        }

        for(int i=0;i<10;i++)
            completed[i] = 0;

        return completed;
    }

    public String getProgress() {
        return "" + filled + "/" + target;
    }
    public String getTotalMoves() {
        return "" + target;
    }

    public int getProgressInt() {
        return target - filled;
    }

    public int[][] getFinalBoard(){
        int[][] finalBoard = new int[9][9];

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(fillBoard[i][j] != -1)
                    finalBoard[i][j] = fillBoard[i][j];

                if(board[i][j] != -1)
                    finalBoard[i][j] = board[i][j];
            }
        }

        return finalBoard;
    }

    protected void setEvents(){
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int tryX = (int) (event.getX() / cellSize);
                int tryY = (int) (event.getY() / cellSize);

                if(tryY < 0 || tryY > 8 || tryX < 0 || tryY > 8)
                    return false;
                else{
                    selectedX = tryX;
                    selectedY = tryY;

                    selectedNum = -1;
                    if(board[selectedX][selectedY] != -1)
                        selectedNum = board[selectedX][selectedY];
                    if(fillBoard[selectedX][selectedY] != -1)
                        selectedNum = fillBoard[selectedX][selectedY];

                    for(int i=0;i<9;i++){
                        for(int j=0;j<9;j++){
                            failBoard[i][j] = -1;
                        }
                    }

                    drawView();
                }

                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    return true;
                }
                return false;
            }
        });
    }



    boolean isValid(int num, int x, int y){
        for(int i=0;i<9;i++){
            if(board[i][y] == num)
                return false;
            if(board[x][i] == num)
                return false;
        }

        int superCellX = (x / 3) * 3;
        int superCellY = (y / 3) * 3;

        for(int i=superCellX;i<superCellX + 3;i++){
            for(int j=superCellY;j<superCellY + 3;j++){
                if(board[i][j] == num)
                    return false;
            }
        }

        return true;
    }

    protected void fillInitialBoard(int cellsNum){
        while(cellsNum > 0){
            final int cellNum = new Random().nextInt(81);
            int cellX = cellNum % 9;
            int cellY = cellNum / 9;

            if(board[cellX][cellY] == -1){
                int start = new Random().nextInt(9);
                boolean found = false;
                for(int i=start;i<start + 9;i++){
                    if(isValid(i % 9 + 1, cellX, cellY)){
                        board[cellX][cellY] = i % 9 + 1;
                        cellsNum -= 1;
                        found = true;
                        break;
                    }
                }
            }
        }
    }

    public void drawView(){
        this.invalidate();
    }

    public void insertNum(int num){

        if(selectedY == -1 || selectedX == -1 || board[selectedX][selectedY] != -1)
            return;

        if(num == 0){
            fillBoard[selectedX][selectedY] = -1;
            failBoard[selectedX][selectedY] = -1;
            drawView();
            return;
        }

        if(isValid(num, selectedX, selectedY)){
            fillBoard[selectedX][selectedY] = num;
            failBoard[selectedX][selectedY] = -1;
        }
        else{
            fillBoard[selectedX][selectedY] = -1;
            failBoard[selectedX][selectedY] = num;
        }

        selectedNum = num;

        filled = 0;

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(fillBoard[i][j] != -1)
                    filled++;
            }
        }

        drawView();
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        boardSize = (int) (Math.min(this.getMeasuredWidth(), this.getMeasuredHeight()) * 1.0f);
        cellSize = (int) (boardSize / 9.0f);
        groupSize = (int) (boardSize / 3.0f);

        setMeasuredDimension(boardSize, boardSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /// background
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundBrush);
        /// highlighted cells

        int cellY = (cellSize * selectedY);
        int cellX = (cellSize * selectedX);

        canvas.drawRect(cellX, 0, cellSize + cellX, boardSize, selectedBrush);
        canvas.drawRect(0, cellY, boardSize, cellSize + cellY, selectedBrush);
        canvas.drawRect(cellX, cellY, cellSize + cellX, cellSize + cellY, superSelectedBrush);

        if(selectedNum != -1)
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(board[i][j] == selectedNum || fillBoard[i][j] == selectedNum){
                        int superCellX = i * cellSize;
                        int superCellY = j * cellSize;
                        canvas.drawRect(superCellX, superCellY, cellSize + superCellX, cellSize + superCellY, superSelectedBrush);
                    }
                }
            }

        /// nums
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j] != -1){
                    drawOriginText(canvas, "" + board[i][j], (cellSize * i) + (cellSize / 2), (cellSize * (j + 1)), 0.525f, 0.32f, textBoldBrush);
                }

                if(fillBoard[i][j] != -1){
                    drawOriginText(canvas, "" + fillBoard[i][j], (cellSize * i) + (cellSize / 2), (cellSize * (j + 1)), 0.525f, 0.32f, textBrush);
                }

                if(failBoard[i][j] != -1){
                    drawOriginText(canvas, "" + failBoard[i][j], (cellSize * i) + (cellSize / 2), (cellSize * (j + 1)), 0.525f, 0.32f, textFailedBrush);
                }
            }
        }
        /// lines
        for(int i=1;i<9;i++){
            canvas.drawLine(cellSize * i, 0, cellSize * i, boardSize, thinLineBrush);
            canvas.drawLine(0, cellSize * i, boardSize, cellSize * i, thinLineBrush);
        }

        for(int i=0;i<4;i++){
            canvas.drawLine(groupSize * i, 0, groupSize * i, boardSize, thickLineBrush);
            canvas.drawLine(0, groupSize * i, boardSize, groupSize * i, thickLineBrush);
        }
    }


    protected void drawOriginText(Canvas canvas, String text, int x, int y, float originX, float originY, Paint brush){
        Rect bounds = new Rect();
        brush.getTextBounds(text, 0, text.length(), bounds);

        int thisX = x - (int) (brush.measureText(text) * originX);
        int thisY = y - (int) (bounds.height() * originY);

        canvas.drawText(text, thisX, thisY, brush);
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        /*
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
        */
    }
}
