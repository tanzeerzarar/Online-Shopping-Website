package com.y4j.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.y4j.dao.SellerRepository;
import com.y4j.entity.ShopOwner;

public class SellerDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private SellerRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ShopOwner user =  userRepository.getSellerByName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user");
		}
		
		CustomSellerDetails customUserDetails = new CustomSellerDetails(user);
		
		return customUserDetails;
	}

	
}
