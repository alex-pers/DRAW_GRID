package by.drawgrid.library.model;

import android.graphics.Canvas;

abstract public class Element {

	public int ID;
	final public int TYPE;
	boolean active;
	float DPI_COEFICIENT = 1;
	
	public Element(int type){
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
