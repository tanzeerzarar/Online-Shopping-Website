package com.y4j.config.both;

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


@Configuration
@Order(1)
public class MyConfig5 extends WebSecurityConfigurerAdapter {

	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder5() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getSellerDetailsService5() {
		
		return new SellerDetailsServiceImpl5();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider5() {

		DaoAuthenticationProvider daoAuthenticationProvider5 = new DaoAuthenticationProvider();
		daoAuthenticationProvider5.setUserDetailsService(this.getSellerDetailsService5());;
		daoAuthenticationProvider5.setPasswordEncoder(passwordEncoder5());
		return daoAuthenticationProvider5;

	}
	//configure method...

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider5());
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/sl/**").hasRole("SELLER")
		.antMatchers("/**").permitAll().and()
		.formLogin().loginPage("/signin-seller")
		.loginProcessingUrl("/dologinseller")
		.defaultSuccessUrl("/sl/dashboard/0")
		.and().csrf().disable();
	}

}