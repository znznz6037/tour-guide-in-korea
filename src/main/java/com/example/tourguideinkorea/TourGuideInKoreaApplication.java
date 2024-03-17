package com.example.tourguideinkorea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TourGuideInKoreaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourGuideInKoreaApplication.class, args);
	}

}
