package com.generic.retailer;

public class DVD implements Item{

	@Override
	public Double price() {
		return 15D;
	}
	
	@Override
	public String toString() {
		return "DVD";
	}
	
}
