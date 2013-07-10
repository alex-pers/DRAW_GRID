package com.example.bla.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;

import com.example.bla.myInterface.InterfaceManagerElement;

public class ManagerElement implements InterfaceManagerElement {

	HashMap<Integer, ArrayList<Element>> allElement;
	int id = 0;

	public ManagerElement() {
		allElement = new HashMap<Integer, ArrayList<Element>>();
	}

	@Override
	public Element findElemtntById(int id) {

		Iterator myVeryOwnIterator = allElement.keySet().iterator();
		while (myVeryOwnIterator.hasNext()) {
			int type = (Integer) myVeryOwnIterator.next();
			ArrayList<Element> value = (ArrayList<Element>) allElement
					.get(type);
			for (int i = 0; i < value.size(); i++) {
				if (value.get(i).ID == id) {
					return value.get(i);
				}
			}
		}

		return null;
	}

	@Override
	public void addElement(Element element) {
		ArrayList<Element> arrElements = allElement.get(element.TYPE);
		element.ID = ++id;
		if (arrElements != null) {
			arrElements.add(element);
		} else {
			ArrayList<Element> arrayList = new ArrayList<Element>();
			arrayList.add(element);
			allElement.put(element.TYPE, arrayList);
		}

	}

	@Override
	public void addElement(Element[] element) {
		for (Element elem : element) {
			ArrayList<Element> arrElements = allElement.get(elem.TYPE);
			elem.ID = ++id;
			if (arrElements != null) {
				arrElements.add(elem);
			} else {
				ArrayList<Element> arrayList = new ArrayList<Element>();
				arrayList.add(elem);
				allElement.put(elem.TYPE, arrayList);
			}
		}

	}

	@Override
	public void addElement(ArrayList<Element> element) {

		for (Element elem : element) {
			ArrayList<Element> arrElements = allElement.get(elem.TYPE);
			elem.ID = ++id;
			
			if (arrElements != null) {
				elem.ID = arrElements.get(arrElements.size() - 1).ID;
				arrElements.add(elem);
			} else {
				ArrayList<Element> arrayList = new ArrayList<Element>();
				arrayList.add(elem);
				allElement.put(elem.TYPE, arrayList);

			}
		}

	}

	@Override
	public ArrayList<Element> getAllElementsByType(int type) {
		return allElement.get(type);
	}

	@Override
	public void drawAllElements(Canvas canvas) {
		Iterator myVeryOwnIterator = allElement.keySet().iterator();
		while (myVeryOwnIterator.hasNext()) {
			int type = (Integer) myVeryOwnIterator.next();
			List<Element> value = (List<Element>) allElement.get(type);
			for (int i = 0; i < value.size(); i++) {
				value.get(i).draw(canvas);
			}
		}

	}

	@Override
	public void drawElementsByType(int[] types, Canvas canvas) {

		for (int t : types) {
			List<Element> value = (List<Element>) allElement.get(t);
			for (Element element : value) {
				element.draw(canvas);
			}
			
		}
		
	}

	@Override
	public void clearAllElement() {
		Iterator myVeryOwnIterator = allElement.keySet().iterator();
		while (myVeryOwnIterator.hasNext()) {
			int type = (Integer) myVeryOwnIterator.next();
			List<Element> value = (List<Element>) allElement.get(type);
			value.clear();
		}
		
	}

}
