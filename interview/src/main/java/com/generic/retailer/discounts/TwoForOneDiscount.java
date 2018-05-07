package com.generic.retailer.discounts;

import java.text.DecimalFormat;

import com.generic.retailer.Item;
import com.generic.retailer.Trolley;

public class TwoForOneDiscount<T extends Item> extends Discount<Item> {

	private T t;
	private DecimalFormat dfFormat;
	private long dvdCount;
	private Double discountAmount;

	public TwoForOneDiscount(T t, Trolley trolley) {
		this.t = t;
		dvdCount = trolley.getItems().stream().filter(e -> e.toString().equalsIgnoreCase("DVD")).count();
		dfFormat = new DecimalFormat("####0.00");
	}

	private Double calc(Long count) {
		if(count > 1){
			long actualCountForCost = (count / 2);
			return actualCountForCost * getBasePrice(t);
		}return 0D;
	}

	@Override
	public String renderInReport() {
		discountAmount = calc(dvdCount);
		return String.format("%1$s %2$12s", "2 FOR 1", "-£" + dfFormat.format(discountAmount));
	}

	@Override
	public Double getAmount() {
		return discountAmount;
	}

}
