package com.vodafone.iot.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NoProductFoundExceptionTest {

	@Test
	void testNoProductFoundException() {

		NoProductFoundException exception = new NoProductFoundException("No product found");

		assertEquals("No product found", exception.getMessage());
	}

}
