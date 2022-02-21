package com.vodafone.iot.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InvalidFilePathExceptionTest {

	@Test
	void testInvalidFilePathException() {

		InvalidFilePathException exception = new InvalidFilePathException("Invalid Path");

		assertEquals("Invalid Path", exception.getMessage());
	}

}
