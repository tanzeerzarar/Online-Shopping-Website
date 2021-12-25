package com.y4j.config.both;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class AllConfig {

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public UserDetailsService getUserDetailsService() {
		
		return new UserDetailsServiceImpl();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());;
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;

	}
	//configure method...


	@Configuration
	public  class CustomerConfiguration extends WebSecurityConfigurerAdapter 
	{
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			//mine
			http.authorizeRequests()
			.antMatchers("/cx/**").hasRole("CUSTOMER")
			.antMatchers("/sl/**").hasRole("SELLER")
			.antMatchers("/cr/**").hasRole("COURIER")
			.antMatchers("/ad/**").hasRole("ADMIN")
			.and()
			.formLogin().loginPage("/signin").loginProcessingUrl("/dologin")
			.permitAll()
			.and().csrf().disable();
		}
	
	}	
}