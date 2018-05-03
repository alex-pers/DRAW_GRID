package by.drawgrid.library.model;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import by.drawgrid.library.view.kotlin.GridCalculator;

abstract public class Element {

	public int ID;
	final public int TYPE;
	boolean active;
	float dpi = 1;

	public GridCalculator gridCalculator;

	public Element(Context context, int type){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		dpi = dm.density;
		this.TYPE = type;
	}

	abstract public void draw(Canvas canvas);

	abstract public int getColor();

	abstract public void setColor(int color);

	abstract public float getWidthLine();

	abstract public void setWidthLine(float widthLine);

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}




}
