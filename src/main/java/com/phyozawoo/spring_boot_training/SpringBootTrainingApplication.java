package com.phyozawoo.spring_boot_training;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Training",
				description = "Spring Boot Project for training",
				version = "v1",
				contact = @Contact(
						name = "Phyo Zaw Oo",
						email = "zawoo389p@prestigein.com"
				)
		)


)
public class SpringBootTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTrainingApplication.class, args);
	}

}
