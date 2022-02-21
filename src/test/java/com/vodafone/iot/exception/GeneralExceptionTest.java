package com.vodafone.iot.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GeneralExceptionTest {

	@Test
	void testGeneralException() {
		GeneralException exception = new GeneralException("General Exception");

		assertEquals("General Exception", exception.getMessage());
	}

}
