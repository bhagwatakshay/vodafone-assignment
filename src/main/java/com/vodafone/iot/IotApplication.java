package com.vodafone.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Vodafone IOT APIs", version = "1.0", description = "APIs to load the data into the java in-built collections or db and get the status of device based on productId and timeStamp.", contact = @Contact(name = "Akshay Bhagwat", email = "bhagwatakki@gmail.com")))
public class IotApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotApplication.class, args);
	}

}