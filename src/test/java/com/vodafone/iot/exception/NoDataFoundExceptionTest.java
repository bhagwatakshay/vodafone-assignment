package com.vodafone.iot.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NoDataFoundExceptionTest {

	@Test
	void testNoDataFoundException() {

		NoDataFoundException exception = new NoDataFoundException("Data not found");

		assertEquals("Data not found", exception.getMessage());

	}

}
