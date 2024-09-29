package com.example.bidMarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BidMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidMarketApplication.class, args);
	}

}

//Xây dựng khung model và các API crud của
// User và authen và author (chú ý có dùng DTO request và response)