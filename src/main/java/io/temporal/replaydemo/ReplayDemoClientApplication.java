package io.temporal.replaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReplayDemoClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReplayDemoClientApplication.class, args).start();;
	}
}
