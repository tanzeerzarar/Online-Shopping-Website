package com.y4j.config.admin;

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
//@Order(20)
public class MyConfig4 extends WebSecurityConfigurerAdapter {

	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder4() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getAdminDetailsService() {
		
		return new AdminDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider4() {

		DaoAuthenticationProvider daoAuthenticationProvider2 = new DaoAuthenticationProvider();
		daoAuthenticationProvider2.setUserDetailsService(this.getAdminDetailsService());;
		daoAuthenticationProvider2.setPasswordEncoder(passwordEncoder4());
		return daoAuthenticationProvider2;

	}
	//configure method...

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider4());
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/ad/**").hasRole("ADMIN")
		.antMatchers("/**").permitAll().and()
		.formLogin().loginPage("/signin-admin")
		.loginProcessingUrl("/dologin-admin")
		.defaultSuccessUrl("/ad/dashboard/0")
		.and().csrf().disable();
	}

}