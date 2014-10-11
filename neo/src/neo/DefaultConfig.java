package neo;

import java.util.HashMap;

import neo.out.arrangement.Accompagnement;
import neo.out.arrangement.Arrangement;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class DefaultConfig {

	@Bean
	public HashMap<String, Object> parameters() {
		return new HashMap<>();
	}
	
}
