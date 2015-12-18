package com.nscc.gameoflife.jareds_machine;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import android.os.Handler;

public class Board extends View {
    // size of grid
    private static final int HORIZONTAL_CELLS = 40;
    private static final int VERTICAL_CELLS = 40;
    // grid line objects
    private ArrayList<Line> verticalLines = new ArrayList<>();
    private ArrayList<Line> horizontalLines = new ArrayList<>();
    protected Cell[][] cells;
    // drawing objects
    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;
    private Paint mPaint;
    protected int color;

    protected boolean addingCells = true;

    int temp = 0; //testing something..

    public Board(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        cells = new Cell[HORIZONTAL_CELLS][VERTICAL_CELLS];

        // setup a default paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }



    // main draw method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.LTGRAY);

        // Draw Grid
        for (int i = 0;i< horizontalLines.size();i++)
        {
            horizontalLines.get(i).draw(canvas, mPaint);
        }
        for (int i = 0;i< verticalLines.size();i++)
        {
            verticalLines.get(i).draw(canvas, mPaint);
        }

        mPaint.setColor(color);

        if (cells[1][1] != null) // just a not null check
        {
            for (int row = 0;row < cells.length;row++) {
                for (int col = 0;col < cells.length;col++)
                {
                    cells[row][col].draw(canvas, mPaint, color);
                }
            }
        }
    }

    public void resetLineAndCellArrays()
    {
        int w = this.getWidth();
        int h = this.getHeight();

        // clear anything currently there
        verticalLines.clear();
        horizontalLines.clear();

        // setup vertical lines
        int cellWidth = w / HORIZONTAL_CELLS;
        for (int i = 0; i < HORIZONTAL_CELLS; i++)
        {
            // new line
            int x1 = cellWidth * i;
            int y1 = 0; // start at top of screen
            int x2 = x1; // vertical lines have same x values
            int y2 = h; // go to bottom of page
            verticalLines.add(new Line(x1, x2, y1, y2));
        }

        // setup horizontal lines
        int cellHeight = h / VERTICAL_CELLS;
        for (int i = 0; i < VERTICAL_CELLS; i++)
        {
            // new line
            int x1 = 0;
            int y1 = cellHeight * i;
            int x2 = w;
            int y2 = y1;
            horizontalLines.add(new Line(x1, x2, y1, y2));
        }

        // cell objects
        for (int row = 0;row < cells.length;row++) {
            for (int col = 0;col < cells.length;col++)
            {
                if (row == 0 || row == HORIZONTAL_CELLS-1 || col == 0 || col == VERTICAL_CELLS-1)
                    cells[row][col] = new Cell(verticalLines.get(row).x1, horizontalLines.get(col).y1, cellWidth, cellHeight, true);
                else
                    cells[row][col] = new Cell(verticalLines.get(row).x1, horizontalLines.get(col).y1, cellWidth, cellHeight, false);
            }
        }
    }

    // override onSizeChanged
    // setup canvas
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void clearCanvas() {
        resetLineAndCellArrays();
        invalidate();
    }

    //override the onTouchEvent
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        // TODO add new cells by touch here...
//        Toast.makeText(context, x + ", " + y, Toast.LENGTH_SHORT).show();
//
//        return true;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mGestureDetector.onTouchEvent(event);

        if (addingCells)
        {
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();

            int cellWidth = this.getWidth() / HORIZONTAL_CELLS;
            int cellHeight = this.getHeight() / VERTICAL_CELLS;

            int cellX = touchX / cellWidth;
            int cellY = touchY / cellHeight;

            if (cellX < HORIZONTAL_CELLS && cellY < VERTICAL_CELLS) {
                if (!cells[cellX][cellY].sentinel) {
                    cells[cellX][cellY].alive = true;
                }
            }

            invalidate();
        }

        return true;
        //return super.onTouchEvent(event);
    }


}