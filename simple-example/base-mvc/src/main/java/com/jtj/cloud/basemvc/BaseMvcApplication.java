package com.jtj.cloud.basemvc;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringCloudApplication
public class BaseMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseMvcApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Base Mvc Started !!";
	}

}
