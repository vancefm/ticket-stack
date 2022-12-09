package com.vancefm.ticketstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketStackApplication {

	public static void main(String[] args) {
		System.setProperty("org.jooq.no-logo", "true");
		SpringApplication.run(TicketStackApplication.class, args);
	}

}
