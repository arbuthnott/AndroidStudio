package com.nscc.gameoflife.jareds_machine;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Line {

    protected int x1 = 0;
    protected int x2 = 0;
    protected int y1 = 0;
    protected int y2 = 0;

    public Line(int x1, int x2, int y1, int y2)
    {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public void draw(Canvas c, Paint p)
    {
        c.drawLine(x1, y1, x2, y2, p);
    }
}
