package com.vodafone.iot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class IotPayloadTest {

	@Test
	void testFilePathGiven() {
		IotPayload iotPayload= new IotPayload();
		iotPayload.setFilepath("C:\\Users\\Akshay\\Desktop\\data.csv");
		
		assertEquals("C:\\Users\\Akshay\\Desktop\\data.csv", iotPayload.getFilepath());
	}

}
