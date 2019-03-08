package edu.hm.ba.classic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Main class for the classic application with HttpSecurity configuration.
 * @author Thomas Großbeck
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ClassicApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ClassicApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.authorizeRequests()
				.antMatchers("/", "/*.js", "/*.map", "/assets/**", "/favicon.ico", "/h2-console/**").permitAll()
				.anyRequest().authenticated()
				.and().httpBasic()
				.and().csrf().disable();
	}

}