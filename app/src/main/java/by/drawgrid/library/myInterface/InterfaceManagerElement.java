package by.drawgrid.library.myInterface;


import android.graphics.Canvas;

import java.util.ArrayList;

import by.drawgrid.library.model.Element;

public interface InterfaceManagerElement {
	
	/**
	 * 
	 * @param element 
	 */
   void	addElement(Element element);
   
   void	addElement(Element[] element);
   
   void	addElement(ArrayList<Element> element);
   
   
   void clearAllElement();
   
   Element findElemtntById(int id);
   ArrayList<Element> getAllElementsByType(int type);
   
   void drawAllElements(Canvas canvas);
   void drawElementsByType(int[] types, Canvas canvas);

}
