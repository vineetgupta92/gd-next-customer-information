package com.gdnext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GdNextCustomerInformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdNextCustomerInformationApplication.class, args);
	}

}
