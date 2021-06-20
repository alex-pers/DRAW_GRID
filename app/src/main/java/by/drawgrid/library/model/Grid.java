package by.drawgrid.library.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import by.drawgrid.library.view.ViewPlusGrid;


public class Grid extends Element {

	public  float sizeSquareX = 10f;
	public  float sizeSquareY = 10f;
	private float marginVerticalRectangle = 40;
	private float marginHorisontalRectangle = 60;
	float textSize = 12;
	Paint paintLine;
	Paint paintText;
	Paint paintRect;
	Rect bounds;
	float countX;
	float countY;
	boolean flagSizeSquareChange = false;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy",Locale.US);


//	public Grid() {
//		super(Constant.TYPE_GRID);
//		init(1);
//	}
	
	public Grid() {
		super(Constant.TYPE_GRID);
		init(ViewPlusGrid.DPI_DENSITY);
	}

	public void setMarginVerticalRectangle(float marginVerticalRectangle) {
		this.marginVerticalRectangle = marginVerticalRectangle;
		marginHorisontalRectangle = (float) (marginVerticalRectangle*1.5);
	}

	void init(float dpi) {
		textSize *=dpi;

		paintLine = new Paint();
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setColor(Color.GRAY);

		paintText = new Paint();
		paintText.setStyle(Paint.Style.FILL_AND_STROKE);
		paintText.setColor(Color.BLACK);
		paintText.setTextSize(textSize);
		
		paintRect = new Paint();
		paintRect.setStyle(Paint.Style.FILL_AND_STROKE); 
		paintRect.setStrokeWidth(1);
		paintRect.setColor(Color.WHITE);
		
		bounds = new Rect();

	}
	
	void calcCountLine(){
		countX = (ViewPlusGrid.decartPointEnd.x - ViewPlusGrid.decartPointStart.x)/ sizeSquareX+1;
		countY = (ViewPlusGrid.decartPointEnd.y - ViewPlusGrid.decartPointStart.y)/ sizeSquareY+1;
		

		if(countX <= Constant.MAX_COUNT_SQUARE_X){
			sizeSquareX/= Constant.SIZE_SQUARE_CHANGER;
			flagSizeSquareChange = true;
		}
		if(countX >= Constant.MIN_COUNT_SQUARE_X){
			sizeSquareX *= Constant.SIZE_SQUARE_CHANGER;
			flagSizeSquareChange = true;
		}
		
		if(countY <= Constant.MAX_COUNT_SQUARE_X){
			sizeSquareY/= Constant.SIZE_SQUARE_CHANGER;
			flagSizeSquareChange = true;
		}
		if(countY >= Constant.MIN_COUNT_SQUARE_X){
			sizeSquareY *= Constant.SIZE_SQUARE_CHANGER;
			flagSizeSquareChange = true;
		}
		
		if(flagSizeSquareChange){
			flagSizeSquareChange = false;
			calcCountLine();
		}
		
	}

	@Override
	public void draw(Canvas canvas) {

		calcCountLine();

		canvas.drawColor(Color.WHITE);

		canvas.drawLine(marginVerticalRectangle, 0, marginVerticalRectangle, ViewPlusGrid.H, paintLine);
		for (int i = 0; i <= countX; i++) {

			float currX = (float) ((Math.floor(ViewPlusGrid.decartPointStart.x/ sizeSquareX)+1)*sizeSquareX) + i*sizeSquareX ;
			
			float x = ViewPlusGrid.DrawKoeficientX*(currX-ViewPlusGrid.decartPointStart.x);
			
			canvas.drawLine(x, 0, x, ViewPlusGrid.H, paintLine);
			

		}
		
		for (int i = 0; i <= countY; i++) {

			float currY =(float) ((Math.floor(ViewPlusGrid.decartPointStart.y/ sizeSquareY)+1)*sizeSquareY) + i*sizeSquareY;
			
			float y =ViewPlusGrid.H- ViewPlusGrid.DrawKoeficientY*((currY/*+i*sizeSquare*/)-ViewPlusGrid.decartPointStart.y);
			
			canvas.drawLine(0,y,ViewPlusGrid.W ,y, paintLine);
		}

	}
	
	public void drawVerticalRange(Canvas canvas){

		canvas.drawRect(0, 0, marginVerticalRectangle, ViewPlusGrid.H- marginVerticalRectangle, paintRect);
		for (int i = 0; i <= countY; i++) {

			float currY =(float) ((Math.floor(ViewPlusGrid.decartPointStart.y/ sizeSquareY)+1)*sizeSquareY) + i*sizeSquareY;
			float y =ViewPlusGrid.H- ViewPlusGrid.DrawKoeficientY*((currY/*+i*sizeSquare*/)-ViewPlusGrid.decartPointStart.y);


			
			if(sizeSquareY >= 1){

				
				if(paintText.measureText(String.format(" %.0f", currY)) > marginVerticalRectangle){
					Paint timePaint = new Paint();

					timePaint.setTextSize(paintText.getTextSize()-2);
					canvas.drawText(String.format(" %.0f $", currY), 0, y,timePaint);
					
				}else{
					
					canvas.drawText(String.format(" %.0f $", currY), 0, y,paintText);
				}
				
			
			}else{
				
				if(paintText.measureText(String.format(" %.3f $", currY))> marginVerticalRectangle){
					Paint timePaint = new Paint();
					timePaint.setTextSize(paintText.getTextSize()-2);
					canvas.drawText(String.format(" %.3f $", currY), 0, y,timePaint);
				}else{
					canvas.drawText(String.format(" %.3f $", currY), 0, y,paintText);
				}
				
			}
		}

		canvas.drawRect(0,ViewPlusGrid.H- marginVerticalRectangle, marginVerticalRectangle,ViewPlusGrid.H , paintRect);
	}
	
	public void drawHorizontalRange(Canvas canvas){

		canvas.drawRect(marginVerticalRectangle,ViewPlusGrid.H- marginHorisontalRectangle, ViewPlusGrid.W,ViewPlusGrid.H , paintRect);
		
		for (int i = 0; i <= countX; i++) {

			float currX = (float) ((Math.floor(ViewPlusGrid.decartPointStart.x/ sizeSquareX)+1)*sizeSquareX) + i*sizeSquareX ;
			Date date = new Date((long) currX);

			float x = ViewPlusGrid.DrawKoeficientX*(currX-ViewPlusGrid.decartPointStart.x);
			
				if(sizeSquareX >= 1){

				
				if(paintText.measureText(sdf.format(date)) > marginHorisontalRectangle){
					Paint timePaint = new Paint();
					timePaint.setTextSize(paintText.getTextSize()-2);
					
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(sdf.format(date), x+textSize/2, ViewPlusGrid.H+2, timePaint);
					canvas.restore();
					
				}else{
					
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(sdf.format(date), x+textSize/2, ViewPlusGrid.H+2, paintText);
					canvas.restore();
				}
				
			
			}else{
				
				if(paintText.measureText(sdf.format(date))> marginHorisontalRectangle){
					Paint timePaint = new Paint();
					timePaint.setTextSize(paintText.getTextSize()-5);
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(sdf.format(date), x+textSize/2, ViewPlusGrid.H+2, timePaint);
					canvas.restore();
				}else{
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(sdf.format(date), x+textSize/2, ViewPlusGrid.H+2, paintText);
					canvas.restore();
				}
			}

		}
		canvas.drawLine(marginVerticalRectangle, ViewPlusGrid.H- marginHorisontalRectangle , ViewPlusGrid.W, ViewPlusGrid.H- marginHorisontalRectangle , paintLine);
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
