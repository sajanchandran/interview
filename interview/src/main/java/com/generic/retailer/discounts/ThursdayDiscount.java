package com.generic.retailer.discounts;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.generic.retailer.Item;
import com.generic.retailer.Trolley;

public class ThursdayDiscount<T extends Item> extends Discount<Item>{

	private T t;
	private DecimalFormat dfFormat;
	private LocalDate localDate;
	private Trolley trolley;
	private Double discountAmount;

	public ThursdayDiscount(T t, LocalDate localDate, Trolley trolley) {
		this.t = t;
		this.localDate = localDate;
		this.trolley = trolley;
		dfFormat = new DecimalFormat("####0.00");
	}
	
	private Double calc(Double total) {
		if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
			return (20 * total) / 100;
		}
		return 0D;
	}

	@Override
	public String renderInReport() {
		discountAmount = calc(calculateTotal());
		return String.format("%1$s %2$14s", "THURS", "-£" + dfFormat.format(discountAmount));
	}
	
	private Double calculateTotal() {
		Double total = 0.00;
		for (Item item : trolley.getItems()) {
			total += item.price();
		}
		return total;
	}

	@Override
	public Double getAmount() {
		return discountAmount;
	}
	
}
