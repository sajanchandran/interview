package com.generic.retailer;

public final class Book implements Item {

	@Override
	public Double price() {
		return 5D;
	}
	
	@Override
	public String toString() {
		return "BOOK";
	}

}
