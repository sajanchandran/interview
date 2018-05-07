package com.generic.retailer.discounts;

import com.generic.retailer.Item;

public abstract class Discount<T extends Item> {
	
	public Double getBasePrice(T t){
		return t.price();
	}
	
	public abstract String renderInReport();
	
	public abstract Double getAmount();
	
}

