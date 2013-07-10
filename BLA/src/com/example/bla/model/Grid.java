package com.example.bla.model;

import com.example.bla.view.ViewPlusGrid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Grid extends Element {

	public  float sizeSquareX = 10f;
	public  float sizeSquareY = 10f;
	public float margin = 40;
	float textSize = 12;
	Paint paintLine;
	Paint paintText;
	Paint paintRect;
	Rect bounds;
	float countX;
	float countY;
	boolean flagSizeSquareChange = false;//TODO: если тру, значит изменился размер

//	public Grid() {
//		super(Constant.TYPE_GRID);
//		init(1);
//	}
	
	public Grid() {
		super(Constant.TYPE_GRID);
		init(ViewPlusGrid.DPI_DENSITY);
	}

	void init(float dpi) {
		
//		margin *= dpi;
		textSize *=dpi;

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
	
	void calcCountLine(){
		countX = (ViewPlusGrid.pointMAX.x - ViewPlusGrid.pointMIN.x)/ sizeSquareX+1;
		countY = (ViewPlusGrid.pointMAX.y - ViewPlusGrid.pointMIN.y)/ sizeSquareY+1;
		

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
		
		
		canvas.drawColor(Color.YELLOW);
		for (int i = 0; i <= countX; i++) {

			float currX = (float) ((Math.floor(ViewPlusGrid.pointMIN.x/ sizeSquareX)+1)*sizeSquareX) + i*sizeSquareX ;
			
			float x = ViewPlusGrid.DrawKoeficientX*(currX-ViewPlusGrid.pointMIN.x);
			
			canvas.drawLine(x, 0, x, ViewPlusGrid.H, paintLine);
			

		}
		
		for (int i = 0; i <= countY; i++) {

			float currY =(float) ((Math.floor(ViewPlusGrid.pointMIN.y/ sizeSquareY)+1)*sizeSquareY) + i*sizeSquareY;
			
			float y =ViewPlusGrid.H- ViewPlusGrid.DrawKoeficientY*((currY/*+i*sizeSquare*/)-ViewPlusGrid.pointMIN.y);
			
			canvas.drawLine(0,y,ViewPlusGrid.W ,y, paintLine);
		}

	}
	
	public void drawVerticalRange(Canvas canvas){
		
		canvas.drawRect(0, 0, margin, ViewPlusGrid.H-margin, paintRect);
		for (int i = 0; i <= countY; i++) {

			float currY =(float) ((Math.floor(ViewPlusGrid.pointMIN.y/ sizeSquareY)+1)*sizeSquareY) + i*sizeSquareY;
			float y =ViewPlusGrid.H- ViewPlusGrid.DrawKoeficientY*((currY/*+i*sizeSquare*/)-ViewPlusGrid.pointMIN.y);
			
			
			
			if(sizeSquareY >= 1){

				
				if(paintText.measureText(String.format(" %.0f", currY)) > margin){
					Paint timePaint = new Paint();

					timePaint.setTextSize(paintText.getTextSize()-2);
					canvas.drawText(String.format(" %.0f", currY), 0, y,timePaint);
					
				}else{
					
					canvas.drawText(String.format(" %.0f", currY), 0, y,paintText);
				}
				
			
			}else{
				
				if(paintText.measureText(String.format(" %.3f", currY))>margin){
					Paint timePaint = new Paint();
					timePaint.setTextSize(paintText.getTextSize()-2);
					canvas.drawText(String.format(" %.3f", currY), 0, y,timePaint);
				}else{
					canvas.drawText(String.format(" %.3f", currY), 0, y,paintText);
				}
				
			}
		}

		canvas.drawRect(0,ViewPlusGrid.H-margin, margin,ViewPlusGrid.H , paintRect);
		
	}
	
	public void drawHorizontalRange(Canvas canvas){
		canvas.drawRect(margin,ViewPlusGrid.H-margin, ViewPlusGrid.W,ViewPlusGrid.H , paintRect);
		
		for (int i = 0; i <= countX; i++) {

			float currX = (float) ((Math.floor(ViewPlusGrid.pointMIN.x/ sizeSquareX)+1)*sizeSquareX) + i*sizeSquareX ;
			
			float x = ViewPlusGrid.DrawKoeficientX*(currX-ViewPlusGrid.pointMIN.x);
			
				if(sizeSquareX >= 1){

				
				if(paintText.measureText(String.format(" %.0f", currX)) > margin){
					Paint timePaint = new Paint();
					timePaint.setTextSize(paintText.getTextSize()-2);
					
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(String.format(" %.0f", currX), x, ViewPlusGrid.H+2, timePaint);
					canvas.restore();
					
				}else{
					
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(String.format(" %.0f", currX), x, ViewPlusGrid.H+2, paintText);
					canvas.restore();
				}
				
			
			}else{
				
				if(paintText.measureText(String.format(" %.3f", currX))>margin){
					Paint timePaint = new Paint();
					timePaint.setTextSize(paintText.getTextSize()-5);
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(String.format(" %.3f", currX), x, ViewPlusGrid.H+2, timePaint);
					canvas.restore();
				}else{
					canvas.save();
					canvas.rotate(-90, x, ViewPlusGrid.H);
					canvas.drawText(String.format(" %.3f", currX), x, ViewPlusGrid.H+2, paintText);
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
