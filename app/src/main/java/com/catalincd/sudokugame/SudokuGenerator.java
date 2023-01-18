package com.catalincd.sudokugame;

import java.util.Random;

public class SudokuGenerator {

    private static final int NULL_DIGIT = -1;
    public static int DIFFICULTY = 5;

    public static int[][] generateBoard(){
        int[][] board = new int[9][9];

        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                board[i][j] = NULL_DIGIT;

        for(int q=0;q<3;q++){
            int[] tempArray = getShuffledArray();

            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    board[i + q * 3][j + q * 3] = tempArray[i * 3 + j];
                }
            }
        }

        int[] targets = new int[54];
        int last = 0;

        for(int i=0;i<81;i++){
            int x = i % 9;
            int y = i / 9;

            if(board[x][y] == NULL_DIGIT)
                targets[last++] = i;
        }


        boolean found = false;
        fillBoard(board, targets, 0);
        stripBoard(board, DIFFICULTY);

        return board;
    }

    private static boolean fillBoard(int[][] board, int[] targets, int current){
        if(current == 54)
            return true;

        int[] sample = getShuffledArray();

        int x = targets[current] % 9;
        int y = targets[current] / 9;

        for(int i=0;i<9;i++){
            if(isValid(sample[i], x, y, board)){
                board[x][y] = sample[i];
                boolean found = fillBoard(board, targets, current + 1);
                if(found)
                    return true;
                board[x][y] = NULL_DIGIT;
            }
        }

        return false;
    }

    private static void stripBoard(int[][] board, int stripNum){
        int[] targets = getShuffledList(stripNum);

        for(int i=0;i<stripNum;i++){
            int x = targets[i] % 9;
            int y = targets[i] / 9;

            board[x][y] = NULL_DIGIT;
        }
    }


    private static boolean isValid(int num, int x, int y, int[][] board){
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

    public static int[] getShuffledArray(){
        int[] tempArray = new int[9];

        for(int i=0;i<9;i++)
            tempArray[i] = i + 1;

        shuffleArray(tempArray);

        return tempArray;
    }

    public static int[] getShuffledList(int arrayLen){
        int[] tempArray = new int[81];

        for(int i=0;i<81;i++)
            tempArray[i] = i;

        shuffleArray(tempArray);

        int[] finalArray = new int[arrayLen];

        for(int i=0;i<arrayLen;i++)
            finalArray[i] = tempArray[i];

        return finalArray;
    }

    public static void shuffleArray(int[] array){
        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

}
