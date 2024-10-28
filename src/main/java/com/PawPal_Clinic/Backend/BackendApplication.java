package com.PawPal_Clinic.Backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

//	@Bean
//	public Hibernate5Module hibernate5Module() {
//		Hibernate5Module module = new Hibernate5Module();
//		module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
//		return module;
//	}
//
//	@Bean
//	public ObjectMapper objectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(hibernate5Module());
//		return mapper;
//	}
}
