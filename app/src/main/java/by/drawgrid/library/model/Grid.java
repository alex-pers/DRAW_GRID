package by.drawgrid.library.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Grid extends Element {

    public float sizeSquareX = 10f;
    public float sizeSquareY = 10f;
    public float margin = 40;
    float textSize = 12;
    Paint paintLine;
    Paint paintText;
    Paint paintRect;
    Rect bounds;
    float countX;
    float countY;
    boolean flagSizeSquareChange = false;

//	public Grid() {
//		super(Constant.TYPE_GRID);
//		init(1);
//	}

    public Grid() {
        super(Constant.TYPE_GRID);
        init(gridCalculator.getDPI_DENSITY());
    }

    void init(float dpi) {

//		margin *= dpi;
        textSize *= dpi;

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.GRAY);

        paintText = new Paint();
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(textSize);

        paintRect = new Paint();
        paintRect.setStyle(Paint.Style.FILL_AND_STROKE);
        paintRect.setStrokeWidth(0);
        paintRect.setColor(Color.LTGRAY);

        bounds = new Rect();

    }

    void calcCountLine() {
        countX = (gridCalculator.getPointMAX().x - gridCalculator.getPointMIN().x) / sizeSquareX + 1;
        countY = (gridCalculator.getPointMAX().y - gridCalculator.getPointMIN().y) / sizeSquareY + 1;


        if (countX <= Constant.MAX_COUNT_SQUARE_X) {
            sizeSquareX /= Constant.SIZE_SQUARE_CHANGER;
            flagSizeSquareChange = true;
        }
        if (countX >= Constant.MIN_COUNT_SQUARE_X) {
            sizeSquareX *= Constant.SIZE_SQUARE_CHANGER;
            flagSizeSquareChange = true;
        }

        if (countY <= Constant.MAX_COUNT_SQUARE_X) {
            sizeSquareY /= Constant.SIZE_SQUARE_CHANGER;
            flagSizeSquareChange = true;
        }
        if (countY >= Constant.MIN_COUNT_SQUARE_X) {
            sizeSquareY *= Constant.SIZE_SQUARE_CHANGER;
            flagSizeSquareChange = true;
        }

        if (flagSizeSquareChange) {
            flagSizeSquareChange = false;
            calcCountLine();
        }

    }

    @Override
    public void draw(Canvas canvas) {

        calcCountLine();


        canvas.drawColor(Color.YELLOW);
        for (int i = 0; i <= countX; i++) {

            float currX = (float) ((Math.floor(gridCalculator.getPointMIN().x / sizeSquareX) + 1) * sizeSquareX) + i * sizeSquareX;

            float x = gridCalculator.getDrawKoeficientX() * (currX - gridCalculator.getPointMIN().x);

            canvas.drawLine(x, 0, x, gridCalculator.getH(), paintLine);

        }

        for (int i = 0; i <= countY; i++) {

            float currY = (float) ((Math.floor(gridCalculator.getPointMIN().y / sizeSquareY) + 1) * sizeSquareY) + i * sizeSquareY;

            float y = gridCalculator.getH() - gridCalculator.getDrawKoeficientY() * ((currY/*+i*sizeSquare*/) - gridCalculator.getPointMIN().y);

            canvas.drawLine(0, y, gridCalculator.getW(), y, paintLine);
        }

    }

    public void drawVerticalRange(Canvas canvas) {

        canvas.drawRect(0, 0, margin, gridCalculator.getH() - margin, paintRect);
        for (int i = 0; i <= countY; i++) {

            float currY = (float) ((Math.floor(gridCalculator.getPointMIN().y / sizeSquareY) + 1) * sizeSquareY) + i * sizeSquareY;
            float y = gridCalculator.getH() - gridCalculator.getDrawKoeficientY() * ((currY/*+i*sizeSquare*/) - gridCalculator.getPointMIN().y);


            if (sizeSquareY >= 1) {


                if (paintText.measureText(String.format(" %.0f", currY)) > margin) {
                    Paint timePaint = new Paint();

                    timePaint.setTextSize(paintText.getTextSize() - 2);
                    canvas.drawText(String.format(" %.0f", currY), 0, y, timePaint);

                } else {

                    canvas.drawText(String.format(" %.0f", currY), 0, y, paintText);
                }


            } else {

                if (paintText.measureText(String.format(" %.3f", currY)) > margin) {
                    Paint timePaint = new Paint();
                    timePaint.setTextSize(paintText.getTextSize() - 2);
                    canvas.drawText(String.format(" %.3f", currY), 0, y, timePaint);
                } else {
                    canvas.drawText(String.format(" %.3f", currY), 0, y, paintText);
                }

            }
        }

        canvas.drawRect(0, gridCalculator.getH() - margin, margin, gridCalculator.getH(), paintRect);

    }

    public void drawHorizontalRange(Canvas canvas) {
        canvas.drawRect(margin, gridCalculator.getH() - margin, gridCalculator.getW(), gridCalculator.getH(), paintRect);

        for (int i = 0; i <= countX; i++) {

            float currX = (float) ((Math.floor(gridCalculator.getPointMIN().x / sizeSquareX) + 1) * sizeSquareX) + i * sizeSquareX;

            float x = gridCalculator.getDrawKoeficientX() * (currX - gridCalculator.getPointMIN().x);

            if (sizeSquareX >= 1) {


                if (paintText.measureText(String.format(" %.0f", currX)) > margin) {
                    Paint timePaint = new Paint();
                    timePaint.setTextSize(paintText.getTextSize() - 2);

                    canvas.save();
                    canvas.rotate(-90, x, gridCalculator.getH());
                    canvas.drawText(String.format(" %.0f", currX), x, gridCalculator.getH() + 2, timePaint);
                    canvas.restore();

                } else {

                    canvas.save();
                    canvas.rotate(-90, x, gridCalculator.getH());
                    canvas.drawText(String.format(" %.0f", currX), x, gridCalculator.getH() + 2, paintText);
                    canvas.restore();
                }


            } else {

                if (paintText.measureText(String.format(" %.3f", currX)) > margin) {
                    Paint timePaint = new Paint();
                    timePaint.setTextSize(paintText.getTextSize() - 5);
                    canvas.save();
                    canvas.rotate(-90, x, gridCalculator.getH());
                    canvas.drawText(String.format(" %.3f", currX), x, gridCalculator.getH() + 2, timePaint);
                    canvas.restore();
                } else {
                    canvas.save();
                    canvas.rotate(-90, x, gridCalculator.getH());
                    canvas.drawText(String.format(" %.3f", currX), x, gridCalculator.getH() + 2, paintText);
                    canvas.restore();
                }
            }

        }

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
