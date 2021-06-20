package by.drawgrid.library.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import by.drawgrid.library.model.Constant;
import by.drawgrid.library.model.Dot;
import by.drawgrid.library.model.Element;
import by.drawgrid.library.model.Grid;
import by.drawgrid.library.model.ManagerElement;
import by.drawgrid.library.model.Point;
import by.drawgrid.library.model.Polygon;
import by.drawgrid.library.model.Slider;
import by.drawgrid.library.myInterface.InterfaceManagerElement;


public class ViewPlusGrid extends View {

	public static final int INVALIDATE_VIEW = 1;
	public static final long TIMER_FOR_REDRAW = 30;
	static public Point decartPointEnd;
	static public Point decartPointStart;
	static public float DrawKoeficientX = 1f;
	static public float DrawKoeficientY = 1f;
	static public float DPI_DENSITY = 1f;
	
	static public float W = 100;
	static public float H = 180;

	// FLAGS
	float MAX_SCALE = 10000000000f;
	float MIN_SCALE = 0.0000000001f;
	float DELTA_X_Y_FOR_FINGER = 10;
	boolean flag_event_for_redrow_view = true;
	int flag_scroll_X = 1;//
	int flag_scroll_Y = 1;
	int flag_scale_X = 1;
	int flag_scale_Y = 1;
	
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
	private float startMaxX = 1623974400000f;
	private float startMaxY = 10;

	private float startMinX = 1621382400000f;
	private float startMinY = 4;
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
	float MARGIN = Constant.SIZE_MARGIN ;
	

	// For timer
	Timer myTimer;
	MyTimerTask timerTask;
	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			if(flagAutoScrollRight){
				moveSlider(4, activeSlider);
				invalidate();
			}
			
			if(flagAutoScrollLeft){
				moveSlider(4, activeSlider);
				invalidate();
			}

			if ((msg.what == INVALIDATE_VIEW) && (flag_event_for_redrow_view==true) ) {
				invalidate();
			}
			setFlagForInvalidate(false);
		}
	};

	public static Point calcDrawCoordinate(Point pointDecart) {
		float x = ViewPlusGrid.DrawKoeficientX
				* (pointDecart.x - ViewPlusGrid.decartPointStart.x);
		float y = ViewPlusGrid.H - ViewPlusGrid.DrawKoeficientY
				* (pointDecart.y - ViewPlusGrid.decartPointStart.y);

		return new Point(x, y);
	}
	
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
		
		decartPointEnd = new Point(startMaxX, startMaxY);
		decartPointStart = new Point(startMinX, startMinY);
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		DPI_DENSITY = dm.density;
		
		MARGIN *= DPI_DENSITY;
		DELTA_X_Y_FOR_FINGER *= DPI_DENSITY;
		
		grid = new Grid();
		grid.setMarginVerticalRectangle(MARGIN);
		polygon = new Polygon();
		polygon.dots.addAll(getStupDots());
		
//		setSlider(65);
//		setSlider(55);
		// max min

		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		addElementToManager(polygon);
		myTimer = new Timer();
		timerTask = new MyTimerTask(handler);
	}

	private List<Dot> getStupDots(){
		List<Dot> subDots = new ArrayList<>();
		subDots.add(new Dot(new Point(1621382400000f, 6.2604f)));
		subDots.add(new Dot(new Point(1621468800000f, 6.3473f)));
		subDots.add(new Dot(new Point(1621555200000f, 6.438f)));
		subDots.add(new Dot(new Point(1621814400000f, 6.3851f)));
		subDots.add(new Dot(new Point(1621900800000f, 6.6496f)));
		subDots.add(new Dot(new Point(1621987200000f, 7.02f)));
		subDots.add(new Dot(new Point(1622073600000f, 6.7705f)));
		subDots.add(new Dot(new Point(1622160000000f, 6.8877f)));
		subDots.add(new Dot(new Point(1622505600000f, 6.7479f)));
		subDots.add(new Dot(new Point(1622592000000f, 6.7819f)));
		subDots.add(new Dot(new Point(1622678400000f, 6.574f)));
		subDots.add(new Dot(new Point(1622764800000f, 6.5929f)));
		subDots.add(new Dot(new Point(1623024000000f, 6.6949f)));
		subDots.add(new Dot(new Point(1623110400000f, 6.9859f)));
		subDots.add(new Dot(new Point(1623196800000f, 6.8839f)));
		subDots.add(new Dot(new Point(1623283200000f, 6.7932f)));
		subDots.add(new Dot(new Point(1623369600000f, 6.9179f)));
		subDots.add(new Dot(new Point(1623628800000f, 6.9557f)));
		subDots.add(new Dot(new Point(1623715200000f, 6.8537f)));
		subDots.add(new Dot(new Point(1623801600000f, 6.9103f)));
		subDots.add(new Dot(new Point(1623888000000f, 7.3673f)));
		subDots.add(new Dot(new Point(1623974400000f, 7.02f)));
		return subDots;
	}


	/**
	 * 
	 * @param x внутренней системы координат
	 */
	 public void setSlider(float x){		 
		 if((sliderLeft == null)){
			 
			 sliderLeft = new Slider(x,0);
			 sliderLeft.STATE_SLIDER = Slider.ONE_SLIDER;
			 sliderLeft.marginFromGrid = MARGIN;
			 
		 }else if(sliderRight == null){
			 
			 if(x>=sliderLeft.point.x){
				 sliderRight = new Slider(x,0);
				 sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
				 sliderRight.marginFromGrid = MARGIN;
				 sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
			 }else{
				 sliderRight = new Slider(sliderLeft.point.x, 0);
				 sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
				 sliderLeft = new Slider(x, 0);
				 sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
				 sliderRight.marginFromGrid = MARGIN;
				 sliderLeft.marginFromGrid = MARGIN;
			 }
			 
		 }else if(x>=sliderLeft.point.x){
			 sliderRight = new Slider(x, 0);
			 sliderRight.marginFromGrid = MARGIN;
			 sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
			 sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
		 } else if(x<sliderLeft.point.x){
			 sliderRight = new Slider(sliderLeft.point.x, 0);
			 sliderRight.STATE_SLIDER = Slider.RIGHT_SLIDER;
			 sliderLeft = new Slider(x, 0);
			 sliderLeft.STATE_SLIDER = Slider.LEFT_SLIDER;
			 sliderRight.marginFromGrid = MARGIN;
			 sliderLeft.marginFromGrid = MARGIN;
		 }

	 }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		W = w;
		H = h ;
		calcDrawKoeficient();

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		grid.draw(canvas);
		synchronized (managerElement) {
			managerElement.drawAllElements(canvas);
		}
		
		grid.drawHorizontalRange(canvas);
		drawSliderShadow(canvas);
		drawSliders(canvas);
		grid.drawVerticalRange(canvas);
	}

	void drawSliders(Canvas canvas){
		if(sliderLeft != null){
		sliderLeft.draw(canvas);
		}
		if(sliderRight!= null){
			sliderRight.draw(canvas);
		}
		
	}

	private void drawSliderShadow(Canvas canvas){
	 	if(sliderLeft != null && sliderRight != null){
			Paint paintRect = new Paint();
			paintRect.setStyle(Paint.Style.FILL);
			paintRect.setColor(Color.WHITE);
			paintRect.setAlpha(180);
			canvas.drawRect(0, 0, sliderLeft.pointDraw.x, ViewPlusGrid.H, paintRect);
			canvas.drawRect(sliderRight.pointDraw.x, 0, ViewPlusGrid.W, ViewPlusGrid.H, paintRect);
		}
	}
	
	public void clearDrawFild(Context context){
		
		 MARGIN = Constant.SIZE_MARGIN ;
		sliderLeft = null;
		sliderRight = null;
		managerElement.clearAllElement();
		
		changeMaxMin(new Point(startMaxX, startMaxY),new Point(0,0));
		init(context);
		
		
	}

	void setFlagForInvalidate(boolean b){
		flag_event_for_redrow_view =b;
	}
	
	public void setFlagForScaleX(boolean b){
		flag_scale_X = b==true ? 1:0;
	}
	public void setFlagForScrollX(boolean b){
		flag_scroll_X = b==true ? 1:0;
	}
	
	public void setFlagForScaleY(boolean b){
		flag_scale_Y = b==true ? 1:0;
	}
	public void setFlagForScrollY(boolean b){
		flag_scroll_Y = b==true ? 1:0;
	}

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
			
			
			
			if(fingerInHorizontalMargin(firstFingerDown.x, firstFingerDown.y)){ 
				
				if(sliderLeft != null){
					if (fingerLandOnSlides(firstFingerDown.x, firstFingerDown.y, sliderLeft)) {
						activeSlider =1;
						stateFinger = STATE_MOVE_ELEMENT;
						return true;
					}
				} 
				
				if(sliderRight!=null){ 
					if (fingerLandOnSlides(firstFingerDown.x, firstFingerDown.y, sliderRight)) { 
						activeSlider = 2;
						stateFinger = STATE_MOVE_ELEMENT;
					}
				}
				
				
			} else{
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
				Point pointDot = calkDotCoordinate(event.getX(), event.getY());

//				Dot dot = new Dot(pointDot);
//				polygon.dots.add(dot);
				setSlider(pointDot.x);
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
					
					if(stateFinger == STATE_MOVE_ELEMENT){
						
						if(event.getX(0)>=W-20){
							flagAutoScrollRight = true;
						} else if(event.getX()<=MARGIN+5){
							flagAutoScrollLeft = true;
							
						}else{
							flagAutoScrollRight = false;
							flagAutoScrollLeft = false;
							moveSlider(event.getX(0) - firstFingerDown.x,activeSlider);
							firstFingerDown.x = event.getX(0);
							firstFingerDown.y = event.getY(0);
						}
						
						
//						moveSlider(event.getX(0) - firstFingerDown.x,activeSlider);
//						firstFingerDown.x = event.getX(0);
//						firstFingerDown.y = event.getY(0);
						 
						
					}else{
					
						moveSystem(event.getX(0) - firstFingerDown.x, event.getY(0)
								- firstFingerDown.y);
						firstFingerDown.x = event.getX(0);
						firstFingerDown.y = event.getY(0);
						calcDrawKoeficient();
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
				
				if((flag_scale_X+flag_scale_Y)==2){

				correctScaleP0(s, point);
				}else{
					if(flag_scale_X==1){
						correctScaleX(s, point);
					}
					if(flag_scale_Y ==1){
						correctScaleY(s, point);
					}
				}

			}
			calcDrawKoeficient();
			// invalidate();
			return true;
		}
	};

	private boolean fingerInHorizontalMargin(float x, float y){// если фолс -- не попал, тру - попал
		
		if( y >= H-MARGIN )
			return true;
		
		return false;
	}
	
	private boolean fingerLandOnSlides(float x, float y, Slider slider){// если фолс -- не попал, тру - попал
		
		switch (slider.STATE_SLIDER) {
		case Slider.ONE_SLIDER:
			if( ( x <= slider.pointDraw.x+DELTA_X_Y_FOR_FINGER+slider.coachWidth/2) && (x >= slider.pointDraw.x-DELTA_X_Y_FOR_FINGER-slider.coachWidth/2)){
				return true;
			}
			break;
		case Slider.LEFT_SLIDER:
			if( ( x <= slider.pointDraw.x) && (x >= slider.pointDraw.x-DELTA_X_Y_FOR_FINGER-slider.coachWidth/2)){
				return true;
			}
			break;
		case Slider.RIGHT_SLIDER:
			if( ( x <= slider.pointDraw.x+DELTA_X_Y_FOR_FINGER+slider.coachWidth) && (x >= slider.pointDraw.x)){
				return true;
			}
			break;
		default:
			break;
		}
		
			
		
		return false;
	}
	
	private void moveSlider(float dx,int activeSlider){

		switch (activeSlider) {
		case 1:
			if(flagAutoScrollLeft){
				 moveSystem(dx, 0);//двигаем систему
				 sliderLeft.point= calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);//пересчитываем координаты слайдера
			}else if(flagAutoScrollRight){//если автоскролл вправо
				
				 if(sliderLeft.STATE_SLIDER == Slider.LEFT_SLIDER){// если есть правый
					 if(!((sliderRight.pointDraw.x-sliderLeft.pointDraw.x) > dx)){//проверяем не заехал ли левый слайдер за правый
						 flagAutoScrollRight=false;//запрещаем автоскролл
					 }else{
						 moveSystem(-dx, 0);//двигаем систему
						 sliderLeft.point= calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);//пересчитываем координаты слайдера
					 }
				 }
				 else{
					 moveSystem(-dx, 0);//двигаем систему
					 sliderLeft.point= calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);//пересчитываем координаты слайдера
				
				 }

			} else{
			
				if(sliderLeft.STATE_SLIDER == Slider.ONE_SLIDER){
					sliderLeft.move(dx);
					sliderLeft.point= calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);
				}else if(sliderLeft.STATE_SLIDER == Slider.LEFT_SLIDER){
					 if((sliderRight.pointDraw.x-sliderLeft.pointDraw.x) > dx){
					
					sliderLeft.move(dx);
					sliderLeft.point= calkDotCoordinate(sliderLeft.pointDraw.x, sliderLeft.pointDraw.y);
					 }
				}
				
			}
			
			break;
		case 2:
			if(flagAutoScrollLeft){

				if(!((sliderRight.pointDraw.x-sliderLeft.pointDraw.x) >  -dx)){//проверяем не заехал ли правый за левый
						 flagAutoScrollLeft=false;//запрещаем автоскролл
					 }else{
						 moveSystem( dx, 0);//двигаем систему
						 sliderRight.point= calkDotCoordinate(sliderRight.pointDraw.x, sliderRight.pointDraw.y);//пересчитываем координаты слайдера
					 }

			}else if(flagAutoScrollRight){//если автоскролл вправо

					moveSystem(-dx, 0);//двигаем систему
					sliderRight.point= calkDotCoordinate(sliderRight.pointDraw.x, sliderRight.pointDraw.y);//пересчитываем координаты слайдера

			} else{
			
				 if((sliderRight.pointDraw.x-sliderLeft.pointDraw.x) >  -dx){
					sliderRight.move(dx);
					sliderRight.point= calkDotCoordinate(sliderRight.pointDraw.x, sliderRight.pointDraw.y);
				 }
			}
			break;

		default:
			break;
		}
		
	}
	
	private void moveSystem(float dx, float dy) {
		
		decartPointEnd.x -= flag_scroll_X* dx/DrawKoeficientX;
		decartPointStart.x -= flag_scroll_X* dx/ DrawKoeficientX;

		decartPointEnd.y += flag_scroll_Y* dy/DrawKoeficientY;
		decartPointStart.y += flag_scroll_Y* dy/DrawKoeficientY;
	}

	private void correctScaleP0(float scaleLocal, Point focusPDraw) {
		 correctScaleX(scaleLocal, focusPDraw);
		 correctScaleY(scaleLocal, focusPDraw);
	}

	private void correctScaleX(float scaleLocal, Point focusPDraw) {
		
		Log.d("correctScaleX", "  DrawKoeficientX = "+DrawKoeficientX + "");
		Log.d("correctScaleX", "  scaleLocal = "+scaleLocal + "");
		if((1/DrawKoeficientX< MAX_SCALE && scaleLocal<1 ) || (1/DrawKoeficientX> MIN_SCALE && scaleLocal>1)){// ограничение по скэйлу
			Point pointkoeficient = calculateKoeficientForScale(focusPDraw);

			float kx = pointkoeficient.x;
			float x1 = (decartPointEnd.x- decartPointStart.x) / kx + decartPointStart.x;// точка от которой надо делать скейл в вымышленной системе
			//(kx * decartPointEnd.x + decartPointStart.x) / (1 + kx)

			decartPointEnd.x = (decartPointEnd.x - x1) / scaleLocal + x1;
			decartPointStart.x = (decartPointStart.x - x1) / scaleLocal + x1;
		}

	}

	private void correctScaleY(float scaleLoc, Point focusPDraw) {
		Log.d("view", "DrawKoeficientY = "+DrawKoeficientY);
//		if((grid.sizeSquareY< MAX_SCALE && scaleLoc<1 ) || (grid.sizeSquareY > MIN_SCALE && scaleLoc>1)){
		if((1/DrawKoeficientY< MAX_SCALE && scaleLoc<1 ) || (1/DrawKoeficientY> MIN_SCALE && scaleLoc>1)){
			Point pointkoeficient = calculateKoeficientForScale(focusPDraw);
	
			float ky = pointkoeficient.y;
			float y1 = (ky * decartPointEnd.y + decartPointStart.y) / (1 + ky);
	
			decartPointEnd.y = (decartPointEnd.y - y1) / scaleLoc + y1;
			decartPointStart.y = (decartPointStart.y - y1) / scaleLoc + y1;
		}

	}

	private Point calculateKoeficientForScale(Point px) {
		return new Point((W) / (px.x), (H - px.y) / (px.y));
		// Point((-px.x) / (px.x - W), (H - px.y) / (px.y));
		// X AND Y calculated in different way just for fun
	}

	public static Point calkDotCoordinate(float x, float y) {
		y = H - y;

		float x1 = (x * (decartPointEnd.x - decartPointStart.x) / W) + decartPointStart.x;
		float y1 = (y * (decartPointEnd.y - decartPointStart.y) / H) + decartPointStart.y;
		
		return new Point(x1, y1);

	}
	
	private void calcDrawKoeficient() {
		DrawKoeficientX = (ViewPlusGrid.W / (ViewPlusGrid.decartPointEnd.x - ViewPlusGrid.decartPointStart.x));
		DrawKoeficientY = (ViewPlusGrid.H / (ViewPlusGrid.decartPointEnd.y - ViewPlusGrid.decartPointStart.y));
	}
	
	public void changeMaxMin(Point pMAX, Point pMIN){
		decartPointEnd = pMAX;
		decartPointStart = pMIN;
		calcDrawKoeficient();
		invalidate();
		
	}
	
	public void setManager(InterfaceManagerElement interfaceManagerElement){
		managerElement = interfaceManagerElement;
	}
	
	public InterfaceManagerElement getManager(){
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
