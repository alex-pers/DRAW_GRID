package by.drawgrid.library.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Polygon extends Element {


    public ArrayList<Dot> dots = new ArrayList<Dot>();
    Paint linePaint;
    float widthLine = 2;
    boolean closedLine = false;// if true -- close! false -- open


    public Polygon(Context context) {
        super(context, Constant.TYPE_POLYGON);
        init();
    }

    void init() {

        widthLine *= dpi;
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(widthLine);
    }

    public void addDot( Dot dot){
        dot.gridCalculator = gridCalculator;
        dots.add(dot);

    }

    @Override
    public void draw(Canvas canvas) {
        int currI = 0;

        for (int i = 0; i < dots.size(); i++) {
            float radius = dots.get(i).radiusCircle;
            float widthCircLine = dots.get(i).circleWidht;

            if (i == (dots.size() - 1)) {
                if ((closedLine) && (dots.size() > 2)) {
                    Dot dot1 = dots.get(i);
                    Dot dot2 = dots.get(0);
                    Point p1 = gridCalculator.calcDrawCoordinate(dot1.point);
                    Point p2 = gridCalculator.calcDrawCoordinate(dot2.point);
                    if (lineInDrawRect(dot1, dot2)) {

                        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, linePaint);
                    }
                }
                dots.get(i).draw(canvas);
            } else {
                Dot dot1 = dots.get(currI);
                Dot dot2 = dots.get(i + 1);

                Point p1 = gridCalculator.calcDrawCoordinate(dot1.point);
                Point p2 = gridCalculator.calcDrawCoordinate(dot2.point);

                if (Math.abs(p2.x - p1.x) > radius * 2 || Math.abs(p2.y - p1.y) > radius * 2) {

                    if (lineInDrawRect(dot1, dot2)) {
                        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, linePaint);
                    }

                    dots.get(currI).draw(canvas);
                    currI = i + 1;
                }


            }
//			dots.get(i).draw(canvas);
        }

    }

    private boolean lineInDrawRect(Dot dot1, Dot dot2) {//���� ���-- ������, ���� �����-- ���
        if (Dot.pointInDrawRect(dot1.point, gridCalculator) || Dot.pointInDrawRect(dot2.point, gridCalculator) || Dot.pointInDrawRect(new Point(dot1.point.x, dot2.point.y), gridCalculator) || Dot.pointInDrawRect(new Point(dot1.point.y, dot2.point.x), gridCalculator))
            return true;
        return false;
    }

    @Override
    public int getColor() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setColor(int color) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getWidthLine() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setWidthLine(float widthLine) {
        // TODO Auto-generated method stub

    }

}
