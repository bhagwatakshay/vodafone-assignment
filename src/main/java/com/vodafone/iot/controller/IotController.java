package com.vodafone.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vodafone.iot.exception.InvalidFilePathException;
import com.vodafone.iot.model.IotPayload;
import com.vodafone.iot.model.IotResponse;
import com.vodafone.iot.service.IotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/iot/event/v1")
@Slf4j
public class IotController {

	@Autowired
	private IotService iotService;

	@Operation(summary = "Loads Data", description = "Loads the data from csv file to java inbuilt collection or db", tags = "Post")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successfully loaded the data", content = {
					@Content(mediaType = "application/json", schema = @Schema(example = "{\"description\": \"String\"}")) }),

			@ApiResponse(responseCode = "400", description = "Filepath is null or empty", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Filepath does not have any file", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "File does not have any data or other technical errors", content = @Content(mediaType = "application/json"))

	})
	@PostMapping(value = "/load-data", produces = "application/json")
	public ResponseEntity<IotResponse> loadData(@RequestBody IotPayload payload) throws Exception {

		if (null != payload.getFilepath() && !payload.getFilepath().isBlank()) {

			log.info("processing for filepath: " + payload.getFilepath());
			iotService.processData(payload.getFilepath());
			IotResponse iotResponse = new IotResponse();
			iotResponse.setDescription("data refreshed");
			return ResponseEntity.status(HttpStatus.OK).body(iotResponse);

		} else {

			log.error("file path is null or empty");
			throw new InvalidFilePathException("File path can not be null or empty");
		}

	}

	@Operation(summary = "Get Status", description = "Get the status for the given productId and timeStamp", tags = "Get")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successfully retrived the status", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = IotResponse.class)) }),

			@ApiResponse(responseCode = "404", description = "Device not found for given productId and timestamp", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "If productId and or timeStamp not provided and othe technical errors", content = @Content(mediaType = "application/json"))

	})
	@GetMapping(value = "/status", produces = "application/json")
	public ResponseEntity<IotResponse> getStatus(@RequestParam(value = "ProductId", required = true) String productId,
			@RequestParam(value = "tstmp", required = true) Long timeStamp) throws Exception {

		try {
			IotResponse iotResponse = iotService.getStatusForProduct(productId, timeStamp);
			return ResponseEntity.status(HttpStatus.OK).body(iotResponse);
		} catch (Exception e) {
			log.error("Error in getStatus: {}", e.getMessage());
			throw e;
		}

	}

}
