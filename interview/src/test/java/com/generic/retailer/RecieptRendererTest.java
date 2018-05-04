package com.generic.retailer;

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
		RecieptRenderer rr = new RecieptRenderer(trolley);
		System.out.println(rr.body());
	}
}

