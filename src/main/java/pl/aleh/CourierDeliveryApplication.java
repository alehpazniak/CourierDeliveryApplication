package pl.aleh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages={"pl.aleh"})
public class CourierDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourierDeliveryApplication.class, args);
	}

}
