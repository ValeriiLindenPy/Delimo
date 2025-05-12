package rs.delimo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class DelimoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DelimoApplication.class, args);
	}
}
