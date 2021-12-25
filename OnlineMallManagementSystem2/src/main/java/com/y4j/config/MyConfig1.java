package com.y4j.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//@Configuration
//@Order(4)
public class MyConfig1 extends WebSecurityConfigurerAdapter {


	@Bean
	public static BCryptPasswordEncoder passwordEncoder1() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getUserDetailsService1() {
		
		return new CustomerDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider1() {

		DaoAuthenticationProvider daoAuthenticationProvider1 = new DaoAuthenticationProvider();
		daoAuthenticationProvider1.setUserDetailsService(this.getUserDetailsService1());;
		daoAuthenticationProvider1.setPasswordEncoder(passwordEncoder1());
		return daoAuthenticationProvider1;

	}
	//configure method...

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider1());
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/cx/**").hasRole("CUSTOMER")
		.and()
		.formLogin().loginPage("/signin")
		.loginProcessingUrl("/dologin")
		.and().csrf().disable();

	
	}

}