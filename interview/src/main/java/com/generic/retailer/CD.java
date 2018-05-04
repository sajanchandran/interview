package com.generic.retailer;

public final class CD implements Item{

	@Override
	public Double price() {
		return 10D;
	}
	
	@Override
	public String toString() {
		return "CD";
	}
}
