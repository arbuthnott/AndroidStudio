package com.nscc.gameoflife.jareds_machine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Cell {
    protected int width;//for drawing
    protected int height;//for drawing
    protected int x;//x coordinate in cellGrid
    protected int y;//y coordinate in cellGrid
    protected boolean alive;//true for alive, false for dead
    protected boolean markedAlive;
    protected boolean sentinel;
    protected int age = 0;

    public Cell(int x,int y, int width,int height,boolean sentinel){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.alive = false;
        this.sentinel = sentinel;
    }

    public void lifeCheck(Cell[][] cellGrid, int myX, int myY)
    {

        // Ryans
//        int liveCount = 0;
//        //checks of surrounding cells, incrementing a count of living
//        for(int x = -1;x<=1;x++)
//        {
//            for(int y = -1;y<=1;y++)
//            {
//                if(cellGrid[myX+x][myY+y].sentinel) {
//                    alive = true;
//                    return;
//                }
//                if (cellGrid[myX+x][myY+y].alive)
//                    liveCount++;
//            }
//        }
//        //check case on count
//        if(this.alive)
//        {
//            liveCount--;
//        }
//        switch (liveCount)
//        {
//            case 2:
//                if (this.alive)
//                    alive = true;
//                    break;
//            case 3:
//                alive = true;
//                break;
//            default:
//                alive = false;
//        }


        // myX and myY is the position of this cell in the board's 2d array
        // the below positions are the top left cell in the 3x3 area around this cell
        int x = myX-1;
        int y = myY-1;

        int liveCount = 0;

        if (sentinel) {
            alive = true;
        } else {
            for(int row = x;row < x + 3;row++)
            {
                for(int col = y;col < y+3;col++)
                {
                    if (cellGrid[row][col].alive && !(cellGrid[row][col].sentinel))
                        liveCount++;
                }
            }

            if (alive) {
                // dont count yourself
                liveCount--;

                if (liveCount == 2 || liveCount == 3)
                    markedAlive = true;
                else
                    markedAlive = false;
            } else {
                if (liveCount == 3)
                    markedAlive = true;
                else
                    markedAlive = false;
            }
        }



        if (alive)
            age++;
        else
            age = 0;

    }

    public void updateAlive()
    {
        if (!sentinel)
            alive = markedAlive;
    }

    void draw(Canvas c,Paint p, int color)
    {
        if (alive) {

            if (sentinel)
                p.setColor(Color.BLACK);
            else
                p.setColor(color);

            // fill rect
            p.setStyle(Paint.Style.FILL);
            // set alpha value by cells age
            int alpha = 50 + (age * 10);
            if (alpha > 100)
                alpha = 100;
            p.setAlpha(alpha);

            // draw cell
            c.drawRect(this.x, this.y, x + this.width, y + this.height, p);

        } else {
//            p.setStyle(Paint.Style.FILL);
//            p.setColor(Color.BLACK);
//            c.drawRect(this.x, this.y, x+this.width, y+this.height, p);
        }
    }
}
