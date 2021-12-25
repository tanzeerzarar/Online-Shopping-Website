package com.y4j.config.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.y4j.dao.AdminRepository;
import com.y4j.dao.SellerRepository;
import com.y4j.entity.MallAdmin;
import com.y4j.entity.ShopOwner;

public class AdminDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		MallAdmin user =  adminRepository.getAdminByName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user");
		}
		
		CustomAdminDetails customAdminDetails = new CustomAdminDetails(user);
		
		return customAdminDetails;
	}

	
}
