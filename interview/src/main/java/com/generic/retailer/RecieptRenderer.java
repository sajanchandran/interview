package com.generic.retailer;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.generic.retailer.discounts.Discount;
import com.generic.retailer.discounts.ThursdayDiscount;
import com.generic.retailer.discounts.TwoForOneDiscount;

public class RecieptRenderer {

	private Trolley trolley;
	private LocalDate localDate;
	private double thursdayDiscountAmount;
	private long dvdCount;
	private BufferedWriter writer;
	private DecimalFormat dfFormat;
	private Double dvdDiscountAmount;

	public RecieptRenderer(Trolley trolley, LocalDate localDate, BufferedWriter writer) {
		this.trolley = trolley;
		this.localDate = localDate;
		this.writer = writer;
		dfFormat = new DecimalFormat("####0.00");
		dvdCount = trolley.getItems().stream().filter(e -> e.toString().equalsIgnoreCase("DVD")).count();
	}

	public String header() {
		return "===== RECEIPT ======";
	}

	public String footer() {
		return "====================";
	}

	public String total() {
		return "TOTAL " + String.format("%1$14s", "£" + dfFormat.format(calculateTotal()));
	}

	public void render() throws IOException {
		writer.write(header());
		writer.write(lineSeparator());
		body();
		writer.write(footer());
		writer.write(lineSeparator());
		writer.write(total());
		writer.write(lineSeparator());
	}

	private void body() throws IOException {
		StringBuilder body = null;
		for (String itemName : groupItemAndCount().keySet()) {
			body = new StringBuilder();
			int leftPadding = 19 - itemName(itemName).length();
			body.append(format("%1$s %2$" + leftPadding + "s", itemName(itemName),
					"£" + totalForEachItem(itemName)));
			writer.write(body.toString());
			writer.write(System.lineSeparator());
		}

		Discount<Item> dvdDiscount = new TwoForOneDiscount<Item>(new DVD(), trolley);
		if(dvdCount > 1){
			writer.write(dvdDiscount.renderInReport());
			writer.write(lineSeparator());
			dvdDiscountAmount = dvdDiscount.getAmount();
		}
		
		Discount<Item> thursdayDiscount = new ThursdayDiscount<Item>(new DVD(), localDate, trolley);
		if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			writer.write(thursdayDiscount.renderInReport());
			writer.write(lineSeparator());
			thursdayDiscountAmount = thursdayDiscount.getAmount();
		}

	}

	private String totalForEachItem(String itemName) {
		return dfFormat.format(groupItemAndCount().get(itemName).stream().map(i -> i.price())
				.collect(Collectors.summarizingDouble(e -> e)).getSum());
	}


	private String itemName(String itemName) {
		return formatItemNames(itemName.toString(), groupItemAndCount().get(itemName).size());
	}

	private Double calculateTotal() {
		Double total = 0.00;
		for (Item item : trolley.getItems()) {
			total += item.price();
		}
		if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			total -= thursdayDiscountAmount;
		}
		if (dvdCount > 1) {
			total -= dvdDiscountAmount;
		}

		return total;
	}

	private Map<String, List<Item>> groupItemAndCount() {
		return trolley.getItems().stream().collect(Collectors.groupingBy(Item::toString));
	}

	private String formatItemNames(String itemName, Integer count) {
		if (count > 1) {
			return itemName + " " + "(x" + count + ")";
		}
		return itemName;
	}
}
