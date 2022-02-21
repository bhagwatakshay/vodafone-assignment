package com.vodafone.iot.repository;

import java.util.List;

import com.vodafone.iot.model.IotData;

public interface Repository {

	void save(IotData iotData);
	
	void clearData();
	
	List<IotData> getData();
}
