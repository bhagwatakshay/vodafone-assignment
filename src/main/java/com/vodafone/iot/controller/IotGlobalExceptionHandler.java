package com.vodafone.iot.controller;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vodafone.iot.exception.GeneralException;
import com.vodafone.iot.exception.InvalidFilePathException;
import com.vodafone.iot.exception.NoDataFoundException;
import com.vodafone.iot.exception.NoProductFoundException;
import com.vodafone.iot.model.IotResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class IotGlobalExceptionHandler {

	@ExceptionHandler(value = FileNotFoundException.class)
	public ResponseEntity<IotResponse> handleFileNotFoundException(FileNotFoundException e1) {
		IotResponse iotResponse = new IotResponse();
		iotResponse.setDescription("ERROR: no data file found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iotResponse);

	}

	@ExceptionHandler(value = InvalidFilePathException.class)
	public ResponseEntity<IotResponse> handleInvalidFilePathException(InvalidFilePathException e2) {
		IotResponse iotResponse = new IotResponse();
		iotResponse.setDescription(e2.getMessage());
		if(e2.getMessage().equals("File path can not be null or empty")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iotResponse);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iotResponse);

	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<IotResponse> handleInvalidFilePathException(Exception e3) {
		IotResponse iotResponse = new IotResponse();
		iotResponse.setDescription("ERROR: A technical exception occurred. " + e3.getMessage());
		log.error(iotResponse.getDescription());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(iotResponse);

	}

	@ExceptionHandler(value = NoDataFoundException.class)
	public ResponseEntity<IotResponse> handleNoDataFoundException(NoDataFoundException e4) {
		IotResponse iotResponse = new IotResponse();
		iotResponse.setDescription(e4.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(iotResponse);

	}

	@ExceptionHandler(value = NoProductFoundException.class)
	public ResponseEntity<IotResponse> handleInvalidProductIdException(NoProductFoundException e5) {
		IotResponse iotResponse = new IotResponse();
		iotResponse.setDescription(e5.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iotResponse);

	}

	@ExceptionHandler(value = GeneralException.class)
	public ResponseEntity<IotResponse> handleGeneralException(GeneralException e6) {
		IotResponse iotResponse = new IotResponse();
		iotResponse.setDescription(e6.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iotResponse);

	}

}
