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

//
//@Configuration
//@Order(9)
public class MyConfig3 extends WebSecurityConfigurerAdapter {


	@Bean
	public static BCryptPasswordEncoder passwordEncoder3() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getCourierDetailsService() {
		
		return new CourierDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider3() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getCourierDetailsService());;
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder3());
		return daoAuthenticationProvider;

	}
	//configure method...

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider3());
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/cr/**").hasRole("COURIER")
		.and()
		.formLogin().loginPage("/signin-courier")
		.loginProcessingUrl("/dologin-courier")
		.defaultSuccessUrl("/cr/dashboard/0")
		.and().csrf().disable();

	
	}

}