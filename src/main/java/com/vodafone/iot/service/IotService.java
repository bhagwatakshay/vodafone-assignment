package com.vodafone.iot.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.vodafone.iot.exception.GeneralException;
import com.vodafone.iot.exception.InvalidFilePathException;
import com.vodafone.iot.exception.NoDataFoundException;
import com.vodafone.iot.exception.NoProductFoundException;
import com.vodafone.iot.model.IotData;
import com.vodafone.iot.model.IotResponse;
import com.vodafone.iot.repository.IotRepository;
import com.vodafone.iot.utility.AppConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IotService {

	@Autowired
	private IotRepository iotRepository;

	// read the data from csv file
	public void processData(String filepath) throws Exception {

		try (FileReader filereader = new FileReader(filepath); CSVReader csvReader = new CSVReader(filereader);) {

			log.info("processing the file data");
			iotRepository.clearData();

			if (null != csvReader.readNext() && null != csvReader.peek()) {
				saveData(csvReader);
			} else {
				log.error("File is empty or does not have any data");
				throw new NoDataFoundException("File is empty or does not have any data");
			}

		} catch (FileNotFoundException ex) {
			log.error("File path {} does not have the file", filepath);
			throw new InvalidFilePathException("File path: " + filepath + " does not have the file");
		} catch (Exception e) {
			throw e;
		}

		log.info("All Data saved in database. Total records are: {}", iotRepository.getData().size());

	}

	// save the data to the list collection or db
	private void saveData(CSVReader csvReader) {
		csvReader.iterator().forEachRemaining(nextRecord -> {

			IotData iotData = new IotData();
			// Accessing Values by Column Index and storing in iot object
			iotData.setDateTime(Long.parseLong(nextRecord[0]));
			iotData.setEventId(Integer.parseInt(nextRecord[1]));
			iotData.setProductId(nextRecord[2]);
			iotData.setLatitude(nextRecord[3]);
			iotData.setLongitude(nextRecord[4]);
			iotData.setBattery(Double.parseDouble(nextRecord[5]));
			iotData.setLight(nextRecord[6]);
			iotData.setAirplaneMode(nextRecord[7]);

			iotRepository.save(iotData);
		});

	}

	public IotResponse getStatusForProduct(String productId, Long timeStamp) {

		List<IotData> data = iotRepository.getData();

		List<IotData> allDataForGivenProduct = data.stream().filter(iotData -> iotData.getProductId().equals(productId))
				.collect(Collectors.toList());

		Optional<IotData> iotDataResult = data.stream()
				.filter(iotData -> iotData.getProductId().equalsIgnoreCase(productId)
						&& iotData.getDateTime().equals(timeStamp))
				.findFirst();

		if (iotDataResult.isPresent()) {

			IotResponse iotResponse = new IotResponse();
			iotResponse.setId(productId);
			iotResponse.setName(getProductName(productId));
			iotResponse.setDatetime(getFormattedDateTime(iotDataResult.get().getDateTime()));
			updateLocation(iotDataResult.get(), iotResponse, allDataForGivenProduct);
			updateBatteryStatus(iotDataResult.get(), iotResponse);

			return iotResponse;
		} else {
			// product itself is not there
			String errorMessege = "ERROR: Id " + productId + " not found";
			throw new NoProductFoundException(errorMessege);
		}

	}

	// Updates battery status based on the percentage of the battery remaining
	public void updateBatteryStatus(IotData iotData, IotResponse iotResponse) {

		Double batteryStatus = iotData.getBattery() * 100;

		if (batteryStatus >= 98) {
			iotResponse.setBattery(AppConstant.FULL);
		} else if (batteryStatus >= 60) {
			iotResponse.setBattery(AppConstant.HIGH);
		} else if (batteryStatus >= 40) {
			iotResponse.setBattery(AppConstant.MEDIUM);
		} else if (batteryStatus >= 10) {
			iotResponse.setBattery(AppConstant.LOW);
		} else if (batteryStatus < 10) {
			iotResponse.setBattery(AppConstant.CRITICAl);
		}

	}

	public void updateLocation(IotData iotData, IotResponse iotResponse, List<IotData> allDataForGivenProduct) {

		if (iotData.getAirplaneMode().equalsIgnoreCase(AppConstant.OFF)) {

			if (isAvailable(iotData.getLongitude()) && isAvailable(iotData.getLatitude())) {
				iotResponse.setLon(iotData.getLongitude());
				iotResponse.setLat(iotData.getLatitude());
				iotResponse.setDescription("SUCCESS: Location identified.");
				iotResponse.setStatus(AppConstant.ACTIVE);
			} else {
				throw new GeneralException("ERROR: Device could not be located");
			}

		} else if (iotData.getAirplaneMode().equalsIgnoreCase(AppConstant.ON)) {
			iotResponse.setLon("");
			iotResponse.setLat("");
			iotResponse.setDescription("SUCCESS: Location not available: Plase turn off aiplane mode");
			iotResponse.setStatus(AppConstant.INACTIVE);
		} else {
			throw new GeneralException("Invalid Airplane mode");
		}

		dynamicActivityTracking(iotData.getProductId(), allDataForGivenProduct, iotResponse);

	}

	// check the longitude and latitude for last three entries and set status as N/A
	// or Inactive for CyclePlusTracker
	private void dynamicActivityTracking(String productId, List<IotData> allDataForGivenProduct,
			IotResponse iotResponse) {
		if (getProductName(productId).equals(AppConstant.CYCLE_PLUS_TRACKER)) {
			if (allDataForGivenProduct.size() < 3) {
				iotResponse.setStatus(AppConstant.NA);
			} else {
				checkConsecutiveGpsLocationAndUpdateSatus(allDataForGivenProduct, iotResponse);
			}
		}

	}

	private void checkConsecutiveGpsLocationAndUpdateSatus(List<IotData> allDataForGivenProduct,
			IotResponse iotResponse) {
		int gpsCount = allDataForGivenProduct.size();
		List<IotData> latestThreeGpsLocations = allDataForGivenProduct.subList(gpsCount - 3, gpsCount);
		if (isEqualLongitude(latestThreeGpsLocations) && isEqualLatitude(latestThreeGpsLocations)) {
			iotResponse.setStatus(AppConstant.INACTIVE);
		}
	}

	private boolean isEqualLatitude(List<IotData> latestThreeGpsLocations) {
		return ((latestThreeGpsLocations.get(0).getLatitude().equals(latestThreeGpsLocations.get(1).getLatitude()))
				&& (latestThreeGpsLocations.get(0).getLatitude().equals(latestThreeGpsLocations.get(2).getLatitude())));

	}

	private boolean isEqualLongitude(List<IotData> latestThreeGpsLocations) {

		return ((latestThreeGpsLocations.get(0).getLongitude().equals(latestThreeGpsLocations.get(1).getLongitude()))
				&& (latestThreeGpsLocations.get(0).getLongitude()
						.equals(latestThreeGpsLocations.get(2).getLongitude())));
	}

	private boolean isAvailable(String location) {
		return (null != location && !location.isBlank());
	}

	private String getFormattedDateTime(Long dateTime) {
		if (null != dateTime && dateTime > 0) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			String formatDateTime = dateFormat.format(dateTime);
			return formatDateTime;
		} else {
			throw new GeneralException("Invalid Date");
		}
	}

	public String getProductName(String productId) {

		if (productId.startsWith("WG")) {
			return AppConstant.CYCLE_PLUS_TRACKER;
		} else if (productId.startsWith("69")) {
			return AppConstant.GENERAL_TRACKER;
		} else {
			// throwing exception in case the product is not tracker or not from above
			// mentioned one
			String errorMessege = "ProductId " + productId + " is invalid";
			throw new NoProductFoundException(errorMessege);
		}

	}
}
