package by.drawgrid.library.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import by.drawgrid.library.view.ViewPlusGrid;


public class Dot extends Element {

	Point point;
	
	 public int radiusCircle=5;
	float circleWidht=4;
	Paint paintCircle;
	Paint paintText;

	
	public Dot(){
		super(Constant.TYPE_DOT);
		point = new Point();
		init(ViewPlusGrid.DPI_DENSITY);
	}
	
	
	public Dot(Point p){
		super(Constant.TYPE_DOT);
		point = p;
		init(ViewPlusGrid.DPI_DENSITY);
	}
	
	
	
	
	
	void init( float dpi) {
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
		
		
		if( pointInDrawRect(point)){
		Point pointDraw =  ViewPlusGrid.calcDrawCoordinate(point);
//		float x = ViewPlusGrid.DrawKoeficientX*(point.x-ViewPlusGrid.pointMIN.x);
//		float y = ViewPlusGrid.H-ViewPlusGrid.DrawKoeficientY*(point.y-ViewPlusGrid.pointMIN.y);
		float x = pointDraw.x;
		float y = pointDraw.y;
		
		 
		canvas.drawCircle(x, y , radiusCircle, paintCircle);

		StringBuilder strText = new StringBuilder();
		strText.append(point.x).append(" , ").append(point.y);
		String label = strText.toString();

		Paint paint = new Paint();
		Rect bounds = new Rect();
		paint.getTextBounds(label, 0, label.length(), bounds);

		bounds.width();

		canvas.drawText(label, x- (bounds.width() / 2), y- (radiusCircle + 3 + bounds.height()), paintText);
		}
	}
	
	public static boolean pointInDrawRect(Point point){
		 if((point.x>ViewPlusGrid.pointMAX.x)||(point.y>ViewPlusGrid.pointMAX.y)||(point.x < ViewPlusGrid.pointMIN.x)||(point.y < ViewPlusGrid.pointMIN.y)){
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
