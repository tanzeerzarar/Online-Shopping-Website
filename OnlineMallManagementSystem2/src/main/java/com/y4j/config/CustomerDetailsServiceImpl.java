package com.y4j.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.y4j.dao.UserRepository;
import com.y4j.entity.Customer;

public class CustomerDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Customer Customer =  userRepository.getUserByName(username);
		if(Customer==null)
		{
			throw new UsernameNotFoundException("could not found user");
		}
		
		CustomCustomerDetails customCustomerDetails = new CustomCustomerDetails(Customer);
		
		return customCustomerDetails;
	}

	
}
