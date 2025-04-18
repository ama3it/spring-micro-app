package com.fitness.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication


/*  
   It's an composite annotation that bundles 
   @Configuration – marks the class as a source of bean definitions.
   @EnableAutoConfiguration – enables Spring Boot's auto-configuration mechanism.
   @ComponentScan – allows Spring to scan the package and register components (like @Service, @Repository, @Controller, etc.)
*/
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

}
