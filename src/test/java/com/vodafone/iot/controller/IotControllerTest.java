package com.vodafone.iot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vodafone.iot.exception.InvalidFilePathException;
import com.vodafone.iot.exception.NoProductFoundException;
import com.vodafone.iot.model.IotPayload;
import com.vodafone.iot.model.IotResponse;
import com.vodafone.iot.service.IotService;

class IotControllerTest {

	@Mock
	private IotService iotService;

	@InjectMocks
	private IotController iotController;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testLoadDataSuccess() {
		IotPayload payload = new IotPayload();
		payload.setFilepath("filepath");
		try {
			doNothing().when(iotService).processData(payload.getFilepath());
			ResponseEntity<IotResponse> loadData = iotController.loadData(payload);
			assertEquals(HttpStatus.OK, loadData.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testLoadDataExceptionFilePathEmpty() {
		IotPayload payload = new IotPayload();
		payload.setFilepath("");
		try {
			assertThrows(InvalidFilePathException.class, () -> iotController.loadData(payload));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testLoadDataExceptionFilePathNull() {
		IotPayload payload = new IotPayload();
		payload.setFilepath(null);
		try {
			assertThrows(InvalidFilePathException.class, () -> iotController.loadData(payload));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetStatusSucess() {
		try {
			IotResponse iotResponse = new IotResponse();
			iotResponse.setStatus("Active");
			when(iotService.getStatusForProduct("productId", 1454140641649L)).thenReturn(iotResponse);
			ResponseEntity<IotResponse> status = iotController.getStatus("productId", 1454140641649L);
			assertEquals(HttpStatus.OK, status.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetStatusException() {
		try {
			when(iotService.getStatusForProduct("productId", 1454140641649L))
					.thenThrow(new NoProductFoundException("Product not found"));
			assertThrows(NoProductFoundException.class, () -> iotController.getStatus("productId", 1454140641649L));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
