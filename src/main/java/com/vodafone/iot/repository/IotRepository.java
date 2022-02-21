package com.vodafone.iot.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vodafone.iot.model.IotData;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IotRepository implements Repository {

	private List<IotData> dataTable = new ArrayList<>();

	@Override
	public void save(IotData iotData) {
		dataTable.add(iotData);

	}

	@Override
	public void clearData() {
		dataTable.clear();
		log.info("old data cleared from database");

	}

	@Override
	public List<IotData> getData() {
		return dataTable;
	}

}
