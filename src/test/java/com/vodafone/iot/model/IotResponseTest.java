package com.vodafone.iot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class IotResponseTest {

	@Test
	void test() {
		IotResponse iotResponse = new IotResponse();

		iotResponse.setBattery("Full");
		iotResponse.setDatetime("25/02/2020  04:31:17");
		iotResponse.setDescription("SUCCESS: Location identified.");
		iotResponse.setId("WG11155638");
		iotResponse.setLat("51.5185");
		iotResponse.setLon("-0.1736");
		iotResponse.setName("CyclePlusTracker");
		iotResponse.setStatus("Active");

		assertEquals("Full", iotResponse.getBattery());
		assertEquals("25/02/2020  04:31:17", iotResponse.getDatetime());
		assertEquals("SUCCESS: Location identified.", iotResponse.getDescription());
		assertEquals("WG11155638", iotResponse.getId());
		assertEquals("51.5185", iotResponse.getLat());
		assertEquals("-0.1736", iotResponse.getLon());
		assertEquals("CyclePlusTracker", iotResponse.getName());
		assertEquals("Active", iotResponse.getStatus());
	}

}
