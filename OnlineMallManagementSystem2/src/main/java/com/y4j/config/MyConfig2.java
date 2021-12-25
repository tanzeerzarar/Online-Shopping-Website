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
//@Order(6)
public class MyConfig2 extends WebSecurityConfigurerAdapter {

	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder2() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getSellerDetailsService() {
		
		return new SellerDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider2() {

		DaoAuthenticationProvider daoAuthenticationProvider2 = new DaoAuthenticationProvider();
		daoAuthenticationProvider2.setUserDetailsService(this.getSellerDetailsService());;
		daoAuthenticationProvider2.setPasswordEncoder(passwordEncoder2());
		return daoAuthenticationProvider2;

	}
	//configure method...

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider2());
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