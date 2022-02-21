package com.vodafone.iot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class IotDataTest {

	@Test
	void testIotDataGetAndSet() {

		IotData iotData = new IotData();

		iotData.setAirplaneMode("OFF");
		iotData.setBattery(99.99);
		iotData.setDateTime(1582605077000L);
		iotData.setEventId(1001);
		iotData.setLatitude("51.5185");
		iotData.setLongitude("-0.1736");
		iotData.setLight("ON");
		iotData.setProductId("WG111556381");

		assertEquals("OFF", iotData.getAirplaneMode());
		assertEquals(99.99, iotData.getBattery());
		assertEquals(1582605077000L, iotData.getDateTime());
		assertEquals(1001, iotData.getEventId());
		assertEquals("51.5185", iotData.getLatitude());
		assertEquals("-0.1736", iotData.getLongitude());
		assertEquals("ON", iotData.getLight());
		assertEquals("WG111556381", iotData.getProductId());
	}

}
