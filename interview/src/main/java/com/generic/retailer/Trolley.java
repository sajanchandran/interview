package com.generic.retailer;

import java.util.ArrayList;
import java.util.List;

public final class Trolley {

	private List<Item> items = new ArrayList<Item>();

	public void addItem(String item) {
		switch (item) {
		case "Book":
			items.add(new Book());
			break;
		case "CD":
			items.add(new CD());
			break;
		case "DVD":
			items.add(new DVD());
			break;
		}
	}

	public List<Item> getItems() {
		return items;
	}

}
