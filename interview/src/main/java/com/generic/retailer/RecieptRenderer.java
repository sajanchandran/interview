package com.generic.retailer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RecieptRenderer {

	private Trolley trolley;
	private LocalDate localDate;
	private double discount;
	private double dvdTotal;
	private long dvdCount;
	
	public RecieptRenderer(Trolley trolley, LocalDate localDate) {
		this.trolley = trolley;
		this.localDate = localDate;
		dvdCount = trolley.getItems().stream().filter(e -> e.toString().equalsIgnoreCase("DVD")).count();
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
		
		addThursdayDiscount(body);
		add2For1DiscountOnDVD(body);
		String finalReport = header() + "\n" + body.toString() + footer() + "\n" + total() + "\n";
		return finalReport;
	}

	private void add2For1DiscountOnDVD(StringBuilder body) {
		if(dvdCount  > 1) {
			long freeDvd = (dvdCount / 2) + (dvdCount % 2);
			dvdTotal = freeDvd * 15D;
			body.append(String.format("%1$s %2$13s", "2 FOR 1", "£-" + dvdTotal + "\n"));
		}
	}

	private void addThursdayDiscount(StringBuilder body) {
		if(localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			discount = (20 * calculateTotal())/100;
			body.append(String.format("%1$s %2$15s", "THURS", "£-"+ discount + "\n"));
		}
	}

	private String itemName(String itemName) {
		return formatItemNames(itemName.toString(), groupItemAndCount().get(itemName).size());
	}
	
	private Double calculateTotal(){
		Double total = 0.00;
		for(Item item : trolley.getItems()){
			total += item.price();
		}
		if(localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			total -= discount;
		}
		if(dvdCount > 1) {
			total -= dvdTotal;
		}

		return total;
	}
	
	private Map<String, List<Item>> groupItemAndCount(){
		 return trolley.getItems().stream().collect(Collectors.groupingBy(Item::toString));
	}
	
	private String formatItemNames(String itemName, Integer count){
		if(count > 1) {
			return itemName + " " + "(x" + count +")";
		}
		return itemName;
	}
}

