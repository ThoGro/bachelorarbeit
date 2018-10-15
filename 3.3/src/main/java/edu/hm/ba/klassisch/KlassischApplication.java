package edu.hm.ba.klassisch;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class KlassischApplication {

	public static void main(String[] args) {
		SpringApplication.run(KlassischApplication.class, args);
	}

	@Configuration
	public class WebConfiguration {
		@Bean
		ServletRegistrationBean h2servletRegistration(){
			ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
			registrationBean.addUrlMappings("/console/*");
			return registrationBean;
		}
	}
}
