package com.vodafone.iot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "datetime", "lon", "lat", "status", "battery", "description" })
public class IotResponse {

	private String id;

	private String name;

	private String datetime;

	@JsonProperty("long")
	private String lon;

	private String lat;

	private String status;

	private String battery;

	private String description;

}
