package com.generic.retailer;

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

public class RecieptRenderer {

	private Trolley trolley;
	private LocalDate localDate;
	private double discount;
	private double dvdTotal;
	private double discountToSubtract;
	private long dvdCount;
	private BufferedWriter writer;
	private DecimalFormat dfFormat;
	private List<Discount<Item>> discounts;

	public RecieptRenderer(Trolley trolley, LocalDate localDate, BufferedWriter writer,
			List<Discount<Item>> discounts) {
		this.trolley = trolley;
		this.localDate = localDate;
		this.writer = writer;
		this.discounts = discounts;
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
		StringBuilder body = new StringBuilder();
		for (String itemName : groupItemAndCount().keySet()) {
			body = new StringBuilder();
			int leftPadding = 19 - itemName(itemName).length();
			body.append(String.format("%1$s %2$" + leftPadding + "s", itemName(itemName),
					"£" + dfFormat.format(groupItemAndCount().get(itemName).stream().map(i -> i.price())
							.collect(Collectors.summarizingDouble(e -> e)).getSum())));
			writer.write(body.toString());
			writer.write(System.lineSeparator());
		}

		for (Discount<Item> discount : discounts) {
			writer.write(discount.renderInReport());
			writer.write(lineSeparator());
			Double amount = discount.getAmount();
			discountToSubtract += amount;
		}

//		addThursdayDiscount(body, writer);
	}

	private void writeBuffer(String input) throws IOException {
		writer.write(input);
		writer.write(lineSeparator());
	}

	private void add2For1DiscountOnDVD(StringBuilder body, BufferedWriter writer) throws IOException {
		if (dvdCount > 1) {
			long freeDvd;
			freeDvd = (dvdCount / 2);
			dvdTotal = freeDvd * 15D;
			writer.write(String.format("%1$s %2$12s", "2 FOR 1", "-£" + dfFormat.format(dvdTotal)));
			writer.write(lineSeparator());
		}
	}

	private void addThursdayDiscount(StringBuilder body, BufferedWriter writer2) throws IOException {
		if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			discount = (20 * calculateTotal()) / 100;
			writer.write(String.format("%1$s %2$14s", "THURS", "-£" + dfFormat.format(discount)));
			writer.write(lineSeparator());
		}
	}

	private String itemName(String itemName) {
		return formatItemNames(itemName.toString(), groupItemAndCount().get(itemName).size());
	}

	private Double calculateTotal() {
		Double total = 0.00;
		for (Item item : trolley.getItems()) {
			total += item.price();
		}
		total -= discountToSubtract;
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
