package com.pengembalianservice.pengembalianservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// 1. TAMBAHKAN IMPORT BERIKUT
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // Pastikan anotasi ini ada untuk integrasi Eureka
public class PengembalianserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PengembalianserviceApplication.class, args);
	}

	// 2. TAMBAHKAN METHOD BEAN INI
	@Bean
	@LoadBalanced // <-- Anotasi ini penting agar bisa menggunakan nama service dari Eureka
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}