package com.vodafone.iot.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.vodafone.iot.model.IotData;

class IotRepositoryTest {

	private static IotRepository iotRepository;

	@BeforeAll
	static void beforeAll() {

		iotRepository = new IotRepository();
	}

	@AfterAll
	static void afterAll() {
		iotRepository = null;
	}

	@Test
	void testSave() {

		IotData iotData = new IotData();

		iotData.setAirplaneMode("OFF");
		iotData.setBattery(99.98);
		iotData.setDateTime(1582605077007L);
		iotData.setEventId(1002);
		iotData.setLatitude("41.5185");
		iotData.setLongitude("1.1736");
		iotData.setLight("OFF");
		iotData.setProductId("WG111556382");
		iotRepository.save(iotData);
		assertEquals(1, iotRepository.getData().size());

	}

	@Test
	void testClearData() {
		IotData iotData = new IotData();

		iotData.setAirplaneMode("OFF");
		iotData.setBattery(99.98);
		iotData.setDateTime(1582605077007L);
		iotData.setEventId(1002);
		iotData.setLatitude("41.5185");
		iotData.setLongitude("1.1736");
		iotData.setLight("OFF");
		iotData.setProductId("WG111556382");
		iotRepository.clearData();
		;
		assertEquals(0, iotRepository.getData().size());

	}

	@Test
	void testGetData() {
		IotData iotData = new IotData();
		List<IotData> dataList = new ArrayList<>();
		iotData.setAirplaneMode("OFF");
		iotData.setBattery(99.98);
		iotData.setDateTime(1582605077007L);
		iotData.setEventId(1002);
		iotData.setLatitude("41.5185");
		iotData.setLongitude("1.1736");
		iotData.setLight("OFF");
		iotData.setProductId("WG111556382");
		dataList.add(iotData);
		iotRepository.save(iotData);
		assertEquals(dataList, iotRepository.getData());
	}

}
