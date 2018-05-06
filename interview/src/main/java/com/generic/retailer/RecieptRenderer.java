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

public class RecieptRenderer {

	private Trolley trolley;
	private LocalDate localDate;
	private double discount;
	private double dvdTotal;
	private long dvdCount;
	private BufferedWriter writer;
	private DecimalFormat dfFormat;

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

		add2For1DiscountOnDVD(body, writer);
		addThursdayDiscount(body, writer);
	}

	private void add2For1DiscountOnDVD(StringBuilder body, BufferedWriter writer) throws IOException {
		if (dvdCount > 1) {
			long freeDvd;
			freeDvd = (dvdCount / 2);
			dvdTotal = freeDvd * 15D;
			writer.write(String.format("%1$s %2$12s", "2 FOR 1", "-£" + dfFormat.format(dvdTotal)));
			writer.write(System.lineSeparator());
		}
	}

	private void addThursdayDiscount(StringBuilder body, BufferedWriter writer2) throws IOException {
		if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			discount = (20 * calculateTotal()) / 100;
			writer.write(String.format("%1$s %2$14s", "THURS", "-£" + dfFormat.format(discount)));
			writer.write(System.lineSeparator());
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
		if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			total -= discount;
		}
		if (dvdCount > 1) {
			total -= dvdTotal;
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
