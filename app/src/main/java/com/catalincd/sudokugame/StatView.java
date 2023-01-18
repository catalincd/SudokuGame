package com.catalincd.sudokugame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class StatView extends View {

    String difficulty;
    String moves;
    String time;
    private final Paint strokeBrush = new Paint();
    private final Paint textBrush = new Paint();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        strokeBrush.setStyle(Paint.Style.STROKE);
        strokeBrush.setStrokeWidth(10);
        strokeBrush.setColor(Color.rgb(0.62f, 0.12f, 0.94f));
        strokeBrush.setAntiAlias(true);

        textBrush.setStyle(Paint.Style.FILL);
        textBrush.setColor(Color.rgb(0.62f, 0.12f, 0.94f));
        textBrush.setTextSize(40);
        textBrush.setAntiAlias(true);

    }

    public void setStats(String diff, String mov, String t){
        difficulty = diff;
        moves = mov;
        time = t;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(10, 40, getWidth()-10, getHeight()-10, 10, 10, strokeBrush);

        canvas.drawText("Difficulty", 35, 100, textBrush);
        canvas.drawText("Moves", 35, 180, textBrush);
        canvas.drawText("Time", 35, 260, textBrush);

        drawOriginText(canvas,difficulty, getWidth()-45, 100, 1, 0, textBrush);
        drawOriginText(canvas,moves, getWidth()-45, 180, 1, 0, textBrush);
        drawOriginText(canvas,time, getWidth()-45, 260, 1, 0, textBrush);
    }

    protected void drawOriginText(Canvas canvas, String text, int x, int y, float originX, float originY, Paint brush){
        Rect bounds = new Rect();
        brush.getTextBounds(text, 0, text.length(), bounds);

        int thisX = x - (int) (brush.measureText(text) * originX);
        int thisY = y - (int) (bounds.height() * originY);

        canvas.drawText(text, thisX, thisY, brush);
    }

}
