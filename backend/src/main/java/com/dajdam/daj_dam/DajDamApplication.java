package com.dajdam.daj_dam;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class DajDamApplication {


	public static void main(String[] args) {
		Dotenv dotenv = Dotenv
				.configure()
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

		SpringApplication.run(DajDamApplication.class, args);
	}
}
