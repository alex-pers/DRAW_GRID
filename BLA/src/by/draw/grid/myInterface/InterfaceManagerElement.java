package by.draw.grid.myInterface;

import java.util.ArrayList;

import android.graphics.Canvas;
import by.draw.grid.model.Element;


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
