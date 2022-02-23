package com.vodafone.iot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vodafone.iot.exception.GeneralException;
import com.vodafone.iot.exception.InvalidFilePathException;
import com.vodafone.iot.exception.NoDataFoundException;
import com.vodafone.iot.exception.NoProductFoundException;
import com.vodafone.iot.model.IotData;
import com.vodafone.iot.model.IotResponse;
import com.vodafone.iot.repository.IotRepository;

class IotServiceTest {

	@Mock
	private IotRepository iotRepository;

	@InjectMocks
	private IotService iotService;

	private IotData iotData;

	private List<IotData> iotDataList;

	private IotResponse iotResponse;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);
		iotData = new IotData();

		iotData.setAirplaneMode("OFF");
		iotData.setBattery(0.99);
		iotData.setDateTime(1582605077000L);
		iotData.setEventId(1001);
		iotData.setLatitude("51.5185");
		iotData.setLongitude("-0.1736");
		iotData.setLight("ON");
		iotData.setProductId("WG111556381");

		iotDataList = new ArrayList<>();
		iotDataList.add(iotData);

		iotResponse = new IotResponse();
	}

	@AfterEach
	void AfterEach() {
		iotData = null;
	}

	@Test
	void testProcessDataSucess() {

		try {
			when(iotRepository.getData()).thenReturn(iotDataList);
			iotService.processData("data.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testProcessDataNoDataInFile() {

		try {

			when(iotRepository.getData()).thenReturn(iotDataList);
			assertThrows(NoDataFoundException.class, () -> iotService.processData("data2.csv"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testProcessDataNoSuchFile() {

		try {

			when(iotRepository.getData()).thenReturn(iotDataList);
			assertThrows(InvalidFilePathException.class, () -> iotService.processData("data1.csv"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetStatusForProductSucess() {

		when(iotRepository.getData()).thenReturn(iotDataList);

		IotResponse statusForProduct = iotService.getStatusForProduct("WG111556381", 1582605077000L);

		assertEquals("CyclePlusTracker", statusForProduct.getName());
	}

	@Test
	void testGetStatusForProductProductNotPresent() {

		when(iotRepository.getData()).thenReturn(iotDataList);

		assertThrows(NoProductFoundException.class, () -> iotService.getStatusForProduct("WG11155638", 1582605077000L));
	}

	@Test
	void testBatterStatusFull() {

		iotService.updateBatteryStatus(iotData, iotResponse);

		assertEquals("Full", iotResponse.getBattery());
	}

	@Test
	void testBatterStatusHigh() {

		Double battery = iotData.getBattery();
		iotData.setBattery(0.61);

		iotService.updateBatteryStatus(iotData, iotResponse);

		assertEquals("High", iotResponse.getBattery());
		iotData.setBattery(battery);
	}

	@Test
	void testBatterStatusMedium() {

		Double battery = iotData.getBattery();
		iotData.setBattery(0.55);

		iotService.updateBatteryStatus(iotData, iotResponse);

		assertEquals("Medium", iotResponse.getBattery());
		iotData.setBattery(battery);
	}

	@Test
	void testBatterStatusLow() {

		Double battery = iotData.getBattery();
		iotData.setBattery(0.11);

		iotService.updateBatteryStatus(iotData, iotResponse);

		assertEquals("Low", iotResponse.getBattery());
		iotData.setBattery(battery);
	}

	@Test
	void testBatterStatusCritical() {

		Double battery = iotData.getBattery();
		iotData.setBattery(0.09);

		iotService.updateBatteryStatus(iotData, iotResponse);

		assertEquals("Critical", iotResponse.getBattery());
		iotData.setBattery(battery);
	}

	@Test
	void testUpdateLocationSucess() {

		iotService.updateLocation(iotData, iotResponse, iotDataList);

		assertEquals("SUCCESS: Location identified.", iotResponse.getDescription());

	}

	@Test
	void testUpdateLocationNoLocatonFoundEmpty() {

		String longitude = iotData.getLongitude();
		String latitude = iotData.getLatitude();
		iotData.setLatitude("");
		iotData.setLongitude("");

		assertThrows(GeneralException.class, () -> iotService.updateLocation(iotData, iotResponse, iotDataList));

		iotData.setLatitude(latitude);
		iotData.setLongitude(longitude);

	}
	
	@Test
	void testUpdateLocationNoLocatonFoundNull() {

		String longitude = iotData.getLongitude();
		String latitude = iotData.getLatitude();
		iotData.setLatitude(null);
		iotData.setLongitude(null);

		assertThrows(GeneralException.class, () -> iotService.updateLocation(iotData, iotResponse, iotDataList));

		iotData.setLatitude(latitude);
		iotData.setLongitude(longitude);

	}

	@Test
	void testUpdateLocationAirplaneModeON() {

		iotData.setAirplaneMode("ON");
		iotService.updateLocation(iotData, iotResponse, iotDataList);

		assertEquals("SUCCESS: Location not available: Plase turn off aiplane mode", iotResponse.getDescription());

		iotData.setAirplaneMode("OFF");

	}

	@Test
	void testUpdateLocationInvalidAirplaneMode() {

		iotData.setAirplaneMode("N/A");

		assertThrows(GeneralException.class, () -> iotService.updateLocation(iotData, iotResponse, iotDataList));

		iotData.setAirplaneMode("OFF");

	}

	@Test
	void testUpdateLocationCheckConsecutiveGpsLocationAndUpdateSatus() {

		iotDataList.add(iotData);
		iotDataList.add(iotData);

		iotService.updateLocation(iotData, iotResponse, iotDataList);

		assertEquals("Inactive", iotResponse.getStatus());

		iotDataList.clear();
		iotDataList.add(iotData);

	}

	@Test
	void testGetProductNameCyclePlusTracker() {

		when(iotRepository.getData()).thenReturn(iotDataList);

		IotResponse statusForProduct = iotService.getStatusForProduct("WG111556381", 1582605077000L);

		assertEquals("CyclePlusTracker", statusForProduct.getName());
	}

	@Test
	void testGetProductNameGeneralTracker() {

		String productId = iotDataList.get(0).getProductId();
		iotDataList.get(0).setProductId("6900001001");
		when(iotRepository.getData()).thenReturn(iotDataList);

		IotResponse statusForProduct = iotService.getStatusForProduct("6900001001", 1582605077000L);

		assertEquals("GeneralTracker", statusForProduct.getName());
		iotDataList.get(0).setProductId(productId);
	}

	@Test
	void testGetProductNoProductFoundException() {
		String productId = iotData.getProductId();
		iotData.setProductId("111111");

		assertThrows(NoProductFoundException.class, () -> iotService.updateLocation(iotData, iotResponse, iotDataList));
		iotData.setProductId(productId);
	}

	@Test
	void testGetStatusForProductInvalidDate() {

		Long dateTime = iotDataList.get(0).getDateTime();
		iotDataList.get(0).setDateTime(0L);
		when(iotRepository.getData()).thenReturn(iotDataList);

		assertThrows(NoProductFoundException.class,
				() -> iotService.getStatusForProduct("WG111556381", 1582605077000L));
		iotDataList.get(0).setDateTime(dateTime);
	}
}
