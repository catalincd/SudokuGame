package com.example.sudokugame;

import android.content.Context;
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

public class ButtonsView extends View {

    private final Paint backgroundBrush = new Paint();
    private final Paint buttonStroke = new Paint();
    private final Paint buttonBackground = new Paint();
    private final Paint textBrush = new Paint();

    private int boardBackground;
    private int textColor;
    private int strokeColor;
    private int buttonBackgroundColor;

    private BoardView refBoard;

    int sizeX;
    int sizeY;

    int cellSize;
    final int offset = 15;

    String[] buttons = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "x"};

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ButtonsView(Context context, @Nullable AttributeSet attrb) {
        super(context, attrb);

        boardBackground = Color.rgb(0.9f, 0.9f, 0.9f);
        textColor = Color.rgb(0.1f, 0.1f, 0.1f);
        strokeColor = Color.rgb(0.0f, 0.0f, 0.0f);
        buttonBackgroundColor = Color.rgb(0.9f, 0.9f, 0.9f);

        backgroundBrush.setStyle(Paint.Style.FILL);
        backgroundBrush.setColor(boardBackground);
        backgroundBrush.setAntiAlias(true);

        buttonStroke.setStyle(Paint.Style.STROKE);
        buttonStroke.setColor(strokeColor);
        buttonStroke.setStrokeWidth(8);
        buttonStroke.setAntiAlias(true);

        buttonBackground.setStyle(Paint.Style.FILL);
        buttonBackground.setColor(buttonBackgroundColor);
        buttonBackground.setAntiAlias(true);

        textBrush.setStyle(Paint.Style.FILL);
        textBrush.setColor(textColor);
        textBrush.setTextSize(125);
        //textBrush.setFakeBoldText(true);
        textBrush.setAntiAlias(true);

        setEvents();
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        sizeX = (int) this.getMeasuredWidth();
        sizeY = (int) (sizeX / 5 * 2);

        cellSize = (int) (sizeX / 5);

        setMeasuredDimension(sizeX, sizeY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundBrush);

        for(int i=0;i<10;i++){
            int xFactor = i % 5;
            int yFactor = i / 5;

            int left = xFactor * cellSize + offset;
            int right = (xFactor + 1) * cellSize - offset;
            int top = yFactor * cellSize + offset;
            int bottom = (yFactor + 1) * cellSize - offset;

            int textLeft = xFactor * cellSize + cellSize / 2;
            int textTop = yFactor * cellSize + cellSize / 2;

            //canvas.drawRoundRect(left, top, right, bottom, 10.0f, 10.0f, buttonBackground);
            //canvas.drawRoundRect(left, top, right, bottom, 10.0f, 10.0f, buttonStroke);
            drawOriginText(canvas, buttons[i], textLeft, textTop, 0.525f, -0.5f, textBrush);
        }
    }

    public void drawView(){
        //Log.d("COORDS: ", "" + selectedX + ", " + selectedY);
        this.invalidate();
    }

    public void setEvents(){
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int tryX = (int) (event.getX() / cellSize);
                int tryY = (int) (event.getY() / cellSize);

                if(tryY < 0 || tryY > 1 || tryX < 0 || tryY > 4)
                    return false;
                else{
                    int num = tryY * 5 + tryX;
                    num = (num + 1) % 10;
                    boardInsert(num);

                    drawView();
                }

                if(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS){

                    return true;
                }
                return false;
            }
        });
    }

    public void boardInsert(int num){
        System.out.println("XCOX");
        refBoard.insertNum(num);
    }

    public void setBoardViewRef(BoardView boardView){
        refBoard = boardView;
    }

    protected void drawOriginText(Canvas canvas, String text, int x, int y, float originX, float originY, Paint brush){
        Rect bounds = new Rect();
        brush.getTextBounds(text, 0, text.length(), bounds);

        int thisX = x - (int) (brush.measureText(text) * originX);
        int thisY = y - (int) (bounds.height() * originY);

        canvas.drawText(text, thisX, thisY, brush);
    }
}
