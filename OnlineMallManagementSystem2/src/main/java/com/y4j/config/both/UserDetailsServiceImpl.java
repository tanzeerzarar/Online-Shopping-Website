package com.y4j.config.both;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.y4j.dao.SellerRepository;
import com.y4j.dao.UserRepository;
import com.y4j.dao.UserrRepository;
import com.y4j.entity.Customer;
import com.y4j.entity.ShopOwner;
import com.y4j.entity.User;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserrRepository userrRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SellerRepository sellerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
//		Customer userCustomer =  userRepository.getUserByName(username);
//		ShopOwner userSelller =  sellerRepository.getSellerByName(username);
		User user =  userrRepository.getUserByName(username);

		if(user!=null)
		{
			CustomUserDetails customUserDetails1 = new CustomUserDetails(user);
			return customUserDetails1;
		}
//		else if(userSelller!=null)
//		{
//			CustomUserDetails customUserDetails2 = new CustomUserDetails(userSelller);
//			return customUserDetails2;
//		}
//		else
//		{
			throw new UsernameNotFoundException("could not found user");
		
		
	}

	
}
