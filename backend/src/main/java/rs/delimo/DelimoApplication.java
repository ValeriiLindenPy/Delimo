package rs.delimo;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class DelimoApplication {


	public static void main(String[] args) {
		Dotenv dotenv = Dotenv
				.configure()
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();

		SpringApplication.run(DelimoApplication.class, args);
	}
}
