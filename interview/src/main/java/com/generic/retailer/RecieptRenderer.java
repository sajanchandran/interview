package com.generic.retailer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RecieptRenderer {

	private Trolley trolley;
	
	public RecieptRenderer(Trolley trolley) {
		this.trolley = trolley;
	}
	
	public String header() {
		return "===== RECEIPT ======";
	}

	public String footer() {
		return "====================";
	}

	public String total() {
		return "TOTAL " + String.format("%1$14s", "£" + calculateTotal()); 
	}
	
	public String body(){
		StringBuilder body = new StringBuilder();
		
		for(String itemName : groupItemAndCount().keySet()){
			int leftPadding = 20-itemName(itemName).length();
			body.append(String.format("%1$s %2$"+leftPadding+"s", itemName(itemName), 
					"£"+groupItemAndCount().get(itemName).stream().map(i -> i.price()).collect(Collectors.summarizingDouble(e -> e)).getSum()+"\n"));
		}
		
		System.out.println(header() + "\n" + body.toString() + footer() + "\n" + total());
		return "";
	}

	private String itemName(String itemName) {
		return formatItemNames(itemName.toString(), groupItemAndCount().get(itemName).size());
	}
	
	private Double calculateTotal(){
		Double total = 0.00;
		for(Item item : trolley.getItems()){
			total += item.price();
		}
		return total;
	}
	
	private Map<String, List<Item>> groupItemAndCount(){
		 return trolley.getItems().stream().collect(Collectors.groupingBy(Item::toString));
	}
	
	private String formatItemNames(String itemName, Integer count){
		return itemName + " " + "(x" + count +")";
	}
}

