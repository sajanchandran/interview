package com.generic.retailer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecieptBody {

	private Trolley trolley;

	public RecieptBody(Trolley trolley) {
		this.trolley = trolley;
	}
	
	public RecieptBody render(){
		Map<String, List<Item>> itemsGroup = trolley.getItems().stream().collect(Collectors.groupingBy(Item::toString));
		return this;
	}
}
