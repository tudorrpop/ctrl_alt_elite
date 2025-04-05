package smartParkSwarm.backend.SmartParkSwarm_Back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartParkSwarmBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartParkSwarmBackendApplication.class, args);
	}

}
