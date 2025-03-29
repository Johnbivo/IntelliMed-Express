package com.IntellimedExpress.SpringServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringServerApplication {
	@GetMapping("/")
	public String getMessage() {
		return "Welcome to IntellimedExpress API. Use /api/auth/* endpoints for authentication.";
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringServerApplication.class, args);
	}

}
