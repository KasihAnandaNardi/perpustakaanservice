package com.kasih.peminjamanservice.peminjamanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class PeminjamanserviceApplication {

	// ANOTASI @Bean DIHAPUS DARI SINI
	public static void main(String[] args) {
		SpringApplication.run(PeminjamanserviceApplication.class, args);
	}

	// BEAN YANG BENAR UNTUK RestTemplate ADA DI SINI
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}