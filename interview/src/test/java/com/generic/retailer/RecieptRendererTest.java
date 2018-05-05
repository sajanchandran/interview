package com.generic.retailer;

import java.time.LocalDate;

import org.junit.Test;

public class RecieptRendererTest {

	@Test
	public void renderReceipt(){
		Trolley trolley = new Trolley();
		trolley.addItem("Book");
		trolley.addItem("Book");
		trolley.addItem("Book");
		trolley.addItem("Book");
		trolley.addItem("CD");
		trolley.addItem("CD");
		trolley.addItem("DVD");
		trolley.addItem("DVD");
		RecieptRenderer rr = new RecieptRenderer(trolley, LocalDate.of(2018, 05, 03));
		System.out.println(rr.body());
	}
}

