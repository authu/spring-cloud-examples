package com.jtj.cloud.basemvc;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@EnableFeignClients
@SpringCloudApplication
public class BaseMvcApplication {

	@Resource
	private BaseClient baseClient;

	public static void main(String[] args) {
		SpringApplication.run(BaseMvcApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Base Mvc Started !!";
	}


	@GetMapping("/base")
	public String getBaseClientData(){
		return baseClient.getBaseClientData();
	}

}
