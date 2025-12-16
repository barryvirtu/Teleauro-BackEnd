package com.teleauro.datamanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.teleauro.repository")
@EntityScan("com.teleauro.model")
@ComponentScan("com.teleauro")
public class DatamanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatamanagementApplication.class, args);
	}

}
