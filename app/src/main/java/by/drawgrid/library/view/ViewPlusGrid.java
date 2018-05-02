package by.drawgrid.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import by.drawgrid.library.model.Constant;
import by.drawgrid.library.model.Dot;
import by.drawgrid.library.model.Element;
import by.drawgrid.library.model.Grid;
import by.drawgrid.library.model.ManagerElement;
import by.drawgrid.library.model.Point;
import by.drawgrid.library.model.Polygon;
import by.drawgrid.library.model.Slider;
import by.drawgrid.library.myInterface.InterfaceManagerElement;
import by.drawgrid.library.view.kotlin.GridCalculator;


public class ViewPlusGrid extends View {

    public static final int INVALIDATE_VIEW = 1;
    public static final long TIMER_FOR_REDRAW = 30;

    public GridCalculator calculator;

//	static public Point pointMAX /*= new Point(20, 1);// */;
//	static public Point pointMIN /*= new Point(0, 0);// */;
//
//	static public float DrawKoeficientX = 1f;
//	static public float DrawKoeficientY = 1f;
//	static public float DPI_DENSITY = 1f;
//
//	static public float W = 100;
//	static public float H = 100;
//
//	// FLAGS
//	float MAX_SCALE = 100f;
//	float MIN_SCALE = 0.00001f;
//	float DELTA_X_Y_FOR_FINGER = 10;

    boolean flag_event_for_redrow_view = true;
//	int flag_scroll_X = 1;//
//	int flag_scroll_Y = 1;
//	int flag_scale_X = 1;
//	int flag_scale_Y = 1;

    // for fingers
    int stateFinger = 0;
    static final int STATE_MOVE_GRID = 1;
    static final int STATE_MOVE_ELEMENT = 2;
    static final int STATE_SCALE = 3;
    static final int STATE_POINT = 0;
    static final int STATE_NOTHING = 4;
    Point firstFingerDown = new Point();
    int idFirstFinger;
    private ScaleGestureDetector mScaleDetector;


    // fod draw
    private float startMaxX = 20;
    private float startMaxY = 1;
    boolean flagAutoScrollRight = false;
    boolean flagAutoScrollLeft = false;
    boolean flagautoScrollTop = false;
    boolean flagautoScrollBottom = false;
    InterfaceManagerElement managerElement = new ManagerElement();
    Polygon polygon /* = new Polygon()*/;
    Grid grid /*= new Grid()*/;
    Slider sliderLeft;
    Slider sliderRight;
    int activeSlider = 0; // 0 оба НЕАКТИВНЫ, 1 - активен левый, 2 - активен правый

//    float MARGIN = Constant.SIZE_MARGIN;


    // For timer
    Timer myTimer;
    MyTimerTask timerTask;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (flagAutoScrollRight) {
                moveSlider(4, activeSlider);
                invalidate();
            }

            if (flagAutoScrollLeft) {
                moveSlider(4, activeSlider);
                invalidate();
            }

            if ((msg.what == INVALIDATE_VIEW) && (flag_event_for_redrow_view == true)) {
                invalidate();
            }
            setFlagForInvalidate(false);
        }
    };

//	public static Point calcDrawCoordinate(Point pointDecart) {
//		float x = ViewPlusGrid.DrawKoeficientX
//				* (pointDecart.x - ViewPlusGrid.pointMIN.x);
//		float y = ViewPlusGrid.H - ViewPlusGrid.DrawKoeficientY
//				* (pointDecart.y - ViewPlusGrid.pointMIN.y);
//
//		return new Point(x, y);
//	}

    public void addElementToManager(Element element) {
        managerElement.addElement(element);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        myTimer.schedule(timerTask, 0, TIMER_FOR_REDRAW);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        myTimer.cancel();
    }

    public ViewPlusGrid(Context context) {
        super(context);
        init(context);
    }

    public ViewPlusGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ViewPlusGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context) {

        calculator = new GridCalculator();
        calculator.setPointMAX(new Point(startMaxX, startMaxY));
        calculator.setPointMIN(new Point(0, 0));

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        calculator.setDPI_DENSITY(dm.density);
//
        calculator.setMARGIN(calculator.getMARGIN() * dm.density); //*= DPI_DENSITY;
        calculator.setDELTA_X_Y_FOR_FINGER(calculator.getDELTA_X_Y_FOR_FINGER() * dm.density);

        grid = new Grid();
        grid.margin = calculator.getMARGIN();
        polygon = new Polygon();

        setSlider(65);
        setSlider(55);
        // max min


        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        addElementToManager(polygon);
        myTimer = new Timer();
        timerTask = new MyTimerTask(handler);
    }


    /**
     * @param x внутренней системы координат
     */
    public void setSlider(float x) {
        if ((sliderLeft == null)) {

            sliderLeft = new Slider(x, 0);
            sliderLeft.STATE_SLIDER = Slider.ONE_SLIDER;
            sliderLeft.marginFromGrid = calculator.getMARGIN();
            ;

        } else if (sliderRight == null) {

            if (x >= sliderLeft.point.x) {
                sliderRight = new Slider(x, 0);
                sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
                sliderRight.marginFromGrid = calculator.getMARGIN();
                ;
                sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
            } else {
                sliderRight = new Slider(sliderLeft.point.x, 0);
                sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
                sliderLeft = new Slider(x, 0);
                sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
                sliderRight.marginFromGrid = calculator.getMARGIN();
                ;
                sliderLeft.marginFromGrid = calculator.getMARGIN();
            }

        } else if (x >= sliderLeft.point.x) {
            sliderRight = new Slider(x, 0);
            sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
            sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
        } else if (x < sliderLeft.point.x) {
            sliderRight = new Slider(sliderLeft.point.x, 0);
            sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
            sliderLeft = new Slider(x, 0);
            sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        calculator.setW(w);
        calculator.setH(h);
        calculator.calcDrawKoeficient();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        grid.draw(canvas);
        synchronized (managerElement) {
            managerElement.drawAllElements(canvas);
        }

        grid.drawHorizontalRange(canvas);
        drawSliders(canvas);
        grid.drawVerticalRange(canvas);
    }

    void drawSliders(Canvas canvas) {
        if (sliderLeft != null) {
            sliderLeft.draw(canvas);
        }
        if (sliderRight != null) {
            sliderRight.draw(canvas);
        }

    }

    public void clearDrawFild(Context context) {

        calculator.setMARGIN(Constant.SIZE_MARGIN);
        sliderLeft = null;
        sliderRight = null;
        managerElement.clearAllElement();

        calculator.changeMaxMin(new Point(startMaxX, startMaxY), new Point(0, 0));
        init(context);


    }


    void setFlagForInvalidate(boolean b) {
        flag_event_for_redrow_view = b;
    }

//	public void setFlagForScaleX(boolean b){
//		flag_scale_X = b==true ? 1:0;
//	}
//	public void setFlagForScrollX(boolean b){
//		flag_scroll_X = b==true ? 1:0;
//	}
//
//	public void setFlagForScaleY(boolean b){
//		flag_scale_Y = b==true ? 1:0;
//	}
//	public void setFlagForScrollY(boolean b){
//		flag_scroll_Y = b==true ? 1:0;
//	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setFlagForInvalidate(true);

        mScaleDetector.onTouchEvent(event);// если два пальца, передаётся в
        // scaleListener
        int i = event.getActionIndex();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN://///////////////////////////////////////////////////////////////////////////////////////

                idFirstFinger = event.getPointerId(i);
                firstFingerDown.x = event.getX();
                firstFingerDown.y = event.getY();


                if (calculator.fingerInHorizontalMargin(firstFingerDown.x, firstFingerDown.y)) {

                    if (sliderLeft != null) {
                        if (fingerLandOnSlides(firstFingerDown.x, firstFingerDown.y, sliderLeft)) {
                            activeSlider = 1;
                            stateFinger = STATE_MOVE_ELEMENT;
                            return true;
                        }
                    }

                    if (sliderRight != null) {
                        if (fingerLandOnSlides(firstFingerDown.x, firstFingerDown.y, sliderRight)) {
                            activeSlider = 2;
                            stateFinger = STATE_MOVE_ELEMENT;
                        }
                    }


                } else {
                    stateFinger = STATE_POINT;
                }


                break;

            case MotionEvent.ACTION_POINTER_DOWN://////////////////////////////////////////////////////////////////////////////////

                if (event.getPointerCount() == 2) {
                    stateFinger = STATE_SCALE;
                }

                break;

            case MotionEvent.ACTION_UP:////////////////////////////////////////////////////////////////////////////////////////////
                if (stateFinger == STATE_POINT) {
                    Point pointDot = calculator.calkDotCoordinate(event.getX(), event.getY());

                    Dot dot = new Dot(pointDot);
                    polygon.dots.add(dot);
                    // points.add(dot);
                    Log.d("field",
                            "touch x:" + event.getX() + "  y:" + event.getY()
                                    + "   count:" + event.getPointerCount());
                    // invalidate();

                }
                activeSlider = 0;
                stateFinger = STATE_POINT;
                break;

            case MotionEvent.ACTION_POINTER_UP:///////////////////////////////////////////////////////////////////////////////////
                stateFinger = STATE_NOTHING;
                break;

            case MotionEvent.ACTION_MOVE://///////////////////////////////////////////////////////////////////////////////////////
                Log.d("field", "move");
                switch (event.getPointerCount()) {
                    case 1:
                        if (stateFinger != STATE_NOTHING) {

                            if (stateFinger == STATE_MOVE_ELEMENT) {

                                if (event.getX(0) >= calculator.getW() - 20) {
                                    flagAutoScrollRight = true;
                                } else if (event.getX() <= calculator.getMARGIN() + 5) {
                                    flagAutoScrollLeft = true;

                                } else {
                                    flagAutoScrollRight = false;
                                    flagAutoScrollLeft = false;
                                    moveSlider(event.getX(0) - firstFingerDown.x, activeSlider);
                                    firstFingerDown.x = event.getX(0);
                                    firstFingerDown.y = event.getY(0);
                                }

                            } else {

                                calculator.moveSystem(event.getX(0) - firstFingerDown.x, event.getY(0)
                                        - firstFingerDown.y);
                                firstFingerDown.x = event.getX(0);
                                firstFingerDown.y = event.getY(0);
                                calculator.calcDrawKoeficient();
                                // invalidate();
                                stateFinger = STATE_MOVE_GRID;
                            }
                        }
                        break;

                }
        }


        return true;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (detector.isInProgress()) {
                float s;

                s = (float) ((float) 1 / ((detector.getPreviousSpan()
                        / detector.getCurrentSpan() - 1d) * 0.5d + 1));// current
                // SCALE

                Point point = new Point(detector.getFocusX(),
                        detector.getFocusY());

//				if((flag_scale_X+flag_scale_Y)==2){
//
//				correctScaleP0(s, point);
//				}else{
//					if(flag_scale_X==1){
//						correctScaleX(s, point);
//					}
//					if(flag_scale_Y ==1){
//						correctScaleY(s, point);
//					}
//				}

            }
            calculator.calcDrawKoeficient();
            // invalidate();
            return true;
        }
    }

    ;


    private boolean fingerLandOnSlides(float x, float y, Slider slider) {// если фолс -- не попал, тру - попал

        switch (slider.STATE_SLIDER) {
            case Slider.ONE_SLIDER:
                if ((x <= slider.pointDraw.x + calculator.getDELTA_X_Y_FOR_FINGER() + slider.coachWidth / 2) && (x >= slider.pointDraw.x - calculator.getDELTA_X_Y_FOR_FINGER() - slider.coachWidth / 2)) {
                    return true;
                }
                break;
            case Slider.LEFT_SLIDER:
                if ((x <= slider.pointDraw.x) && (x >= slider.pointDraw.x - calculator.getDELTA_X_Y_FOR_FINGER() - slider.coachWidth / 2)) {
                    return true;
                }
                break;
            case Slider.RIGHT_SLIDER:
                if ((x <= slider.pointDraw.x + calculator.getDELTA_X_Y_FOR_FINGER() + slider.coachWidth) && (x >= slider.pointDraw.x)) {
                    return true;
                }
                break;
            default:
                break;
        }


        return false;
    }

    private void moveSlider(float dx, int activeSlider) {


        switch (activeSlider) {
            case 1:

                if (flagAutoScrollLeft) {
                    calculator.moveSystem(dx, 0);//двигаем систему
                    sliderLeft.point = calculator.calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);//пересчитываем координаты слайдера

                } else if (flagAutoScrollRight) {//если автоскролл вправо

                    if (sliderLeft.STATE_SLIDER == Slider.LEFT_SLIDER) {// если есть правый
                        if (!((sliderRight.pointDraw.x - sliderLeft.pointDraw.x) > dx)) {//проверяем не заехал ли левый слайдер за правый
                            flagAutoScrollRight = false;//запрещаем автоскролл
                        } else {
                            calculator.moveSystem(-dx, 0);//двигаем систему
                            sliderLeft.point = calculator.calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);//пересчитываем координаты слайдера
                        }
                    } else {
                        calculator.moveSystem(-dx, 0);//двигаем систему
                        sliderLeft.point = calculator.calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);//пересчитываем координаты слайдера

                    }


                } else {

                    if (sliderLeft.STATE_SLIDER == Slider.ONE_SLIDER) {
                        sliderLeft.move(dx);
                        sliderLeft.point = calculator.calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);
                    } else if (sliderLeft.STATE_SLIDER == Slider.LEFT_SLIDER) {
                        if ((sliderRight.pointDraw.x - sliderLeft.pointDraw.x) > dx) {

                            sliderLeft.move(dx);
                            sliderLeft.point = calculator.calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);
                        }
                    }

                }

                break;
            case 2:
                if (flagAutoScrollLeft) {

                    if (!((sliderRight.pointDraw.x - sliderLeft.pointDraw.x) > -dx)) {//проверяем не заехал ли правый за левый
                        flagAutoScrollLeft = false;//запрещаем автоскролл
                    } else {
                        calculator.moveSystem(dx, 0);//двигаем систему
                        sliderRight.point = calculator.calkDotCoordinate(sliderRight.pointDraw.x, sliderRight.pointDraw.y);//пересчитываем координаты слайдера
                    }


                } else if (flagAutoScrollRight) {//если автоскролл вправо

                    calculator.moveSystem(-dx, 0);//двигаем систему
                    sliderRight.point = calculator.calkDotCoordinate(sliderRight.pointDraw.x, sliderRight.pointDraw.y);//пересчитываем координаты слайдера


                } else {

                    if ((sliderRight.pointDraw.x - sliderLeft.pointDraw.x) > -dx) {
                        sliderRight.move(dx);
                        sliderRight.point = calculator.calkDotCoordinate(sliderRight.pointDraw.x, sliderRight.pointDraw.y);
                    }
                }
                break;

            default:
                break;
        }

    }

//	private void moveSystem(float dx, float dy) {
//
//
//		Point copyPoint= new Point(pointMAX.x, pointMAX.y);
//
//		pointMAX.x -= flag_scroll_X* dx/DrawKoeficientX /** (copyPoint.x - pointMIN.x) / W*/;
//		pointMIN.x -= flag_scroll_X* dx/ DrawKoeficientX /** (copyPoint.x - pointMIN.x) / W*/;
//
//		pointMAX.y += flag_scroll_Y* dy/DrawKoeficientY /** (copyPoint.y - pointMIN.y) / H*/;
//		pointMIN.y += flag_scroll_Y* dy/DrawKoeficientY /* * (copyPoint.y - pointMIN.y) / H*/;
//
//	}

//	private void correctScaleP0(float scaleLoc, Point focusPDraw) { // scale both direction
//
//		 correctScaleX(scaleLoc, focusPDraw);
//		 correctScaleY(scaleLoc, focusPDraw);
//	}

//	private void correctScaleX(float scaleLoc, Point focusPDraw) {
//
//		Log.d("view", "DrawKoeficientX = "+DrawKoeficientX);
////		if((grid.sizeSquareX < MAX_SCALE && scaleLoc<1 ) || (grid.sizeSquareX > MIN_SCALE && scaleLoc>1)){// ограничение по размеру квадратика
//		if((1/DrawKoeficientX< MAX_SCALE && scaleLoc<1 ) || (1/DrawKoeficientX> MIN_SCALE && scaleLoc>1)){// ограничение по скэйлу
//			Point pointkoeficient = calculateKoeficientForScale(focusPDraw);
//
//			float kx = pointkoeficient.x;
//			float x1 = (kx * pointMAX.x + pointMIN.x) / (1 + kx);
//
//			pointMAX.x = (pointMAX.x - x1) / scaleLoc + x1;
//			pointMIN.x = (pointMIN.x - x1) / scaleLoc + x1;
//		}
//
//	}

//	private void correctScaleY(float scaleLoc, Point focusPDraw) {
//		Log.d("view", "DrawKoeficientY = "+DrawKoeficientY);
////		if((grid.sizeSquareY< MAX_SCALE && scaleLoc<1 ) || (grid.sizeSquareY > MIN_SCALE && scaleLoc>1)){
//		if((1/DrawKoeficientY< MAX_SCALE && scaleLoc<1 ) || (1/DrawKoeficientY> MIN_SCALE && scaleLoc>1)){
//			Point pointkoeficient = calculateKoeficientForScale(focusPDraw);
//
//			float ky = pointkoeficient.y;
//			float y1 = (ky * pointMAX.y + pointMIN.y) / (1 + ky);
//
//			pointMAX.y = (pointMAX.y - y1) / scaleLoc + y1;
//			pointMIN.y = (pointMIN.y - y1) / scaleLoc + y1;
//		}
//
//	}
//
//	private Point calculateKoeficientForScale(Point px) {
//		return new Point((-px.x) / (px.x - W), (H - px.y) / (px.y));
//	}

//    public static Point calkDotCoordinate(float x, float y) {
//        y = H - y;
//
//        float x1 = (x * (pointMAX.x - pointMIN.x) / W) + pointMIN.x;
//        float y1 = (y * (pointMAX.y - pointMIN.y) / H) + pointMIN.y;
//
//        return new Point(x1, y1);
//
//    }

//    private void calcDrawKoeficient() {
//        DrawKoeficientX = (ViewPlusGrid.W / (ViewPlusGrid.pointMAX.x - ViewPlusGrid.pointMIN.x));
//        DrawKoeficientY = (ViewPlusGrid.H / (ViewPlusGrid.pointMAX.y - ViewPlusGrid.pointMIN.y));
//    }

    public void changeMaxMin(Point pMAX, Point pMIN) {
        calculator.changeMaxMin(pMAX, pMIN);
//        pointMAX = pMAX;
//        pointMIN = pMIN;
//        calcDrawKoeficient();
        invalidate();
        //TODO: 28.04.2018 INVALIDATE
    }

    public void setManager(InterfaceManagerElement interfaceManagerElement) {
        managerElement = interfaceManagerElement;
    }

    public InterfaceManagerElement getManager() {
        return managerElement;
    }

    class MyTimerTask extends TimerTask {

        Handler handlerTIMER;

        public MyTimerTask(Handler handler) {
            handlerTIMER = handler;
        }

        public void run() {
            handlerTIMER.sendEmptyMessage(INVALIDATE_VIEW);

            System.out.println("");
        }
    }


}
