package com.y4j.controller;

import java.security.Principal;


import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.y4j.dao.AdminRepository;
import com.y4j.dao.OrderRepository;
import com.y4j.dao.RequestRepository;
import com.y4j.dao.ShopRepository;
import com.y4j.dao.UserRepository;
import com.y4j.dao.UserrRepository;
import com.y4j.entity.MallAdmin;
import com.y4j.entity.Shop;
import com.y4j.entity.ShopRequest;
import com.y4j.entity.User;
import com.y4j.enumm.RequestStatus;
import com.y4j.helper.Message;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Controller
@RequestMapping("ad")
public class AdminController 
{	
	@Autowired
	UserrRepository userrRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	ShopRepository shopRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	RequestRepository requestRepository;
			
	//method to add common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();
		MallAdmin user = adminRepository.getAdminByName(userName);
		System.out.println("------------------------ "+user.getName());
		//System.out.println(user.getRoles()+"---- "+user.getEmail());
		model.addAttribute("user", user);
		
	}
	// home dashboard
	@RequestMapping("/dashboard/{page}")
	public String dashboardAdmin(@PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("dashboard admin");

		String userName = principal.getName();//System.out.println("un-"+userName);
		MallAdmin user = this.adminRepository.getAdminByName(userName);//System.out.println("ma-"+user.getId());
		model.addAttribute("title", "Admin Dashboard");
		Pageable pageable = PageRequest.of(page, 6, Sort.by("id").descending());//, Sort.by("id").descending()	
		Page<ShopRequest> requestList = this.requestRepository.findRequestByAdminId(user.getId(), pageable);
		model.addAttribute("items", requestList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", requestList.getTotalPages());
		return "ad/ad_dashboard";
	}
	
	//profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{System.out.println("Inside profile");
		model.addAttribute("title", "Profile Page");
		return "/ad/profile";
	}

	//settings page
	@RequestMapping("/settings")
	public String settings(Model model, Principal principal)
	{System.out.println("inside settings page");
		
		model.addAttribute("title", "Settings page");
		//check for right user access for the contacts
		return "/ad/settings";		
	}
	//open update form
	@PostMapping("/change-password")
	public String changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Model model, Principal principal)
	{System.out.println("Inside change-password handler");

	
		String userName = principal.getName();
		User user = this.userrRepository.getUserByName(userName);
		System.out.println("pass "+user.getPassword());
		if(this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
		{
			//change the password
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userrRepository.save(user);
			session.setAttribute("message", new Message("Password changed successfully", "success"));

		}
		else
		{
			//error
			session.setAttribute("message", new Message("Old password didn't match", "danger"));

		}
			
			//		model.addAttribute("customer", customer);
		return "redirect:/ad/settings";
	}

	//shop request action
	@RequestMapping("/shop-action/{rid}/{status}")
	public String shopAction(@PathVariable("rid") long rid, @PathVariable("status") Integer status, Model model, Principal principal, HttpSession session)
	{System.out.println("inside shop action handler"+rid);
		
		ShopRequest sr = this.requestRepository.getById(rid);
		switch(status)
		{
			case 0: sr.setRequestStatus(RequestStatus.APPROVED);
			Shop shop = new Shop();
			shop.setMall(sr.getMall());
			shop.setShopCategory(sr.getShopCategory());
			shop.setShopName(sr.getShopname());
			shop.setShopOwner(sr.getShopOwner());
			this.shopRepository.save(shop);
			System.out.println("ordinal-"+status);
			session.setAttribute("message", new Message("You have approved the request", "success"));
			break;
			
			case 1: sr.setRequestStatus(RequestStatus.REJECTED);
				this.requestRepository.save(sr);
				session.setAttribute("message", new Message("You have rejected the request", "success"));
			break;
		}
		
		//check for right user access for the contacts
		return "redirect:/ad/dashboard/0";		
	}
	
}