package com.vodafone.iot.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IotData {

	private Long dateTime;

	private String productId;

	private Integer eventId;

	private String latitude;

	private String longitude;

	private Double battery;

	private String light;

	private String airplaneMode;

}
