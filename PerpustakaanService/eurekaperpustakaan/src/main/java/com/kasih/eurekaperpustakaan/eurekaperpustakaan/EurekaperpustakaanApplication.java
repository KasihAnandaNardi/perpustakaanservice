package com.kasih.eurekaperpustakaan.eurekaperpustakaan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaperpustakaanApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaperpustakaanApplication.class, args);
	}
}
