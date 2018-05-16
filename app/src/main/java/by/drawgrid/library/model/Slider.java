package by.drawgrid.library.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


public class Slider extends Element {

    public static final int ONE_SLIDER = 0;
    public static final int LEFT_SLIDER = 1;
    public static final int RIGHT_SLIDER = 2;

    public Point point = new Point();
    public Point pointDraw = new Point();
    public int STATE_SLIDER = 0;
    int color;
    Paint paintLine;
    Paint paintText;
    Paint paintCoach;
    Path wallpath;
    public float marginFromGrid = 40;
    float lineWidth = 2;
    public float coachWidth = 30;
    float textSize = 12;


    public Slider(Context context) {
        super(context, Constant.TYPE_SLIDER);

        init();
    }

    public Slider(Context context, float x, float y) {
        super(context, Constant.TYPE_SLIDER);
        point.x = x;
        point.y = y;
        pointDraw = gridCalculator.calcDrawCoordinate(point);
        init();
    }

    void init() {
        lineWidth *= gridCalculator.getDPI_DENSITY();
        coachWidth *= gridCalculator.getDPI_DENSITY();
        textSize *= gridCalculator.getDPI_DENSITY();

        color = Color.RED;

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(color);
        paintLine.setStrokeWidth(lineWidth);

        paintText = new Paint();
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setColor(Color.BLUE);
        paintText.setTextSize(textSize);

        paintCoach = new Paint();
        paintCoach.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCoach.setColor(color);

        wallpath = new Path();

    }

    @Override
    public void draw(Canvas canvas) {


        if (STATE_SLIDER == ONE_SLIDER) {

            pointDraw = gridCalculator.calcDrawCoordinate(point);
            float xDraw = pointDraw.x;

            canvas.drawLine(xDraw, 0, xDraw, gridCalculator.getH(), paintLine);

            wallpath.reset(); // only needed when reusing this path for a new build
            wallpath.moveTo(xDraw, gridCalculator.getH() - marginFromGrid); // used for first point
            wallpath.lineTo(xDraw + (coachWidth / 2), gridCalculator.getH() - (marginFromGrid / 2));
            wallpath.lineTo(xDraw + (coachWidth / 2), gridCalculator.getH());
            wallpath.lineTo(xDraw - (coachWidth / 2), gridCalculator.getH());
            wallpath.lineTo(xDraw - (coachWidth / 2), gridCalculator.getH() - (float) (marginFromGrid / 2));
            wallpath.lineTo(xDraw, gridCalculator.getH() - marginFromGrid);
            canvas.drawPath(wallpath, paintCoach);

            String str = String.format(" %.0f", point.x);
            canvas.drawText(str, xDraw - paintText.measureText(str) / 2, (float) (gridCalculator.getH() - (marginFromGrid / 4)), paintText);
        }

        if (STATE_SLIDER == LEFT_SLIDER) {

            pointDraw = gridCalculator.calcDrawCoordinate(point);
            float xDraw = pointDraw.x;

            canvas.drawLine(xDraw, 0, xDraw, gridCalculator.getH(), paintLine);

            wallpath.reset(); // only needed when reusing this path for a new build
            wallpath.moveTo(xDraw, gridCalculator.getH() - marginFromGrid); // used for first point
            wallpath.lineTo(xDraw, gridCalculator.getH());
            wallpath.lineTo(xDraw - coachWidth, gridCalculator.getH());
            wallpath.lineTo(xDraw - coachWidth, gridCalculator.getH() - (marginFromGrid / 2));
            wallpath.lineTo(xDraw, gridCalculator.getH() - marginFromGrid);

            canvas.drawPath(wallpath, paintCoach);

            String str = String.format(" %.0f", point.x);
            canvas.drawText(str, xDraw - coachWidth, (float) (gridCalculator.getH() - (marginFromGrid / 4)), paintText);
        }

        if (STATE_SLIDER == RIGHT_SLIDER) {

            pointDraw = gridCalculator.calcDrawCoordinate(point);
            float xDraw = pointDraw.x;

            canvas.drawLine(xDraw, 0, xDraw, gridCalculator.getH(), paintLine);

            wallpath.reset(); // only needed when reusing this path for a new build
            wallpath.moveTo(xDraw, gridCalculator.getH() - marginFromGrid); // used for first point
            wallpath.lineTo(xDraw, gridCalculator.getH());
            wallpath.lineTo(xDraw + coachWidth, gridCalculator.getH());
            wallpath.lineTo(xDraw + coachWidth, gridCalculator.getH() - (marginFromGrid / 2));
            wallpath.lineTo(xDraw, gridCalculator.getH() - marginFromGrid);

            canvas.drawPath(wallpath, paintCoach);

            String str = String.format(" %.0f", point.x);
            canvas.drawText(str, xDraw, (float) (gridCalculator.getH() - (marginFromGrid / 4)), paintText);
        }


    }


    /**
     * @param dx � �������� ������� ���������
     */
    public void move(float dx) {
        pointDraw.x += dx;
    }

    @Override
    public int getColor() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setColor(int color) {
        this.color = color;

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
