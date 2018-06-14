package by.drawgrid.library.model;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import by.drawgrid.library.view.kotlin.GridCalculator;


public class Dot extends Element {

    Point point;

    public int radiusCircle = 5;
    float circleWidht = 4;
    Paint paintCircle;
    Paint paintText;


    public Dot(Context context) {
        super(context, Constant.TYPE_DOT);
        point = new Point();
        init(dpi);
    }


    public Dot(Point p, Context context) {
        super(context, Constant.TYPE_DOT);
        point = p;
        init(dpi);
    }


    void init(float dpi) {
        circleWidht *= dpi;
        radiusCircle *= dpi;
        paintCircle = new Paint();
        paintText = new Paint();

        paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCircle.setColor(Color.GREEN);
        paintCircle.setStrokeWidth(circleWidht);

        paintText.setStyle(Paint.Style.STROKE);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(16);
    }

    public void draw(Canvas canvas) {


        if (pointInDrawRect(point, gridCalculator)) {
            Point pointDraw = gridCalculator.calcDrawCoordinate(point);
//		float x = ViewPlusGrid.DrawKoeficientX*(point.x-ViewPlusGrid.pointMIN.x);
//		float y = ViewPlusGrid.H-ViewPlusGrid.DrawKoeficientY*(point.y-ViewPlusGrid.pointMIN.y);
            float x = pointDraw.x;
            float y = pointDraw.y;


            canvas.drawCircle(x, y, radiusCircle, paintCircle);

            StringBuilder strText = new StringBuilder();
            strText.append(point.x).append(" , ").append(point.y);
            String label = strText.toString();

            Paint paint = new Paint();
            Rect bounds = new Rect();
            paint.getTextBounds(label, 0, label.length(), bounds);

            bounds.width();

            canvas.drawText(label, x - (bounds.width() / 2), y - (radiusCircle + 3 + bounds.height()), paintText);
        }
    }

    public static boolean pointInDrawRect(Point point, GridCalculator gridCalculator) {
        if ((point.x > gridCalculator.getPointMAX().x) || (point.y > gridCalculator.getPointMAX().y) || (point.x < gridCalculator.getPointMIN().x) || (point.y < gridCalculator.getPointMIN().y)) {
            return false;
        }

        return true;
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
