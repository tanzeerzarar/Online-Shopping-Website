package com.y4j.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.y4j.dao.CourierRepository;
import com.y4j.entity.CourierServiceProvider;

public class CourierDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private CourierRepository courierRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		CourierServiceProvider user =  courierRepository.getCourierByName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user");
		}
		
		CustomCourierDetails customCourierDetails = new CustomCourierDetails(user);
		
		return customCourierDetails;
	}

	
}
