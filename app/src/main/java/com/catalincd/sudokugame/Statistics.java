package com.catalincd.sudokugame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public class Statistics extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String[] stats = readStats().split("\n");
        Collections.reverse(Arrays.asList(stats));

        LinearLayout mainLayout = findViewById(R.id.mainLayout);

        for(int i=0;i<stats.length;i++){
            String[] currentStats = stats[i].split(",");

            StatView newView = new StatView(this, null);
            newView.setStats(currentStats[0], currentStats[1], currentStats[2]);
            newView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    300));

            mainLayout.addView(newView);
        }

        setButtons();
    }

    public void clearStats(){
        Context context = this;
        String str = "";
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

    protected void setButtons() {
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStats();
                LinearLayout mainLayout = findViewById(R.id.mainLayout);
                mainLayout.removeAllViews();
            }
        });
    }
}