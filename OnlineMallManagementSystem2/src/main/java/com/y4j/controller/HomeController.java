package com.y4j.controller;


import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.y4j.dao.CourierRepository;
import com.y4j.dao.OrderRepository;
import com.y4j.dao.SellerRepository;
import com.y4j.dao.UserRepository;
import com.y4j.dao.UserrRepository;
import com.y4j.entity.CourierServiceProvider;
import com.y4j.entity.Customer;
import com.y4j.entity.MallAdmin;
import com.y4j.entity.OrderDetails;
import com.y4j.entity.ShopOwner;
import com.y4j.entity.User;
import com.y4j.helper.Message;


@Controller
public class HomeController 
{
	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private UserrRepository userrRepository;	

	@Autowired
	private CourierRepository courierRepository;	

	@Autowired
	private SellerRepository sellerRepository;	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	@Autowired
	OrderRepository orderRepository;
//
//	//courier signin-admin
//	@RequestMapping("/signin-admin")
//	public String adminLogin(Model model)
//	{System.out.println("inside signin admin-");
//		model.addAttribute("title", "Login Page");
//		return "signin_admin";
//		
//	}

	
//	//courier signin
//	@RequestMapping("/signin-courier")
//	public String courierLogin(Model model)
//	{System.out.println("inside signin cr-");
//		model.addAttribute("title", "Login Page");
//		return "signin_courier";
//		
//	}

	//customer signin
	@RequestMapping("/signin")
	public String customLogin(Model model)
	{System.out.println("inside signin cx-");
		model.addAttribute("title", "Login Page");
		return "signin";
		
	}


	
	@RequestMapping("/home")
	public String home(Model model)
	{
		model.addAttribute("title", "Online Mall");
		return "home";
		
	}
	@RequestMapping("/")
	public String welcome(Model model)
	{
		model.addAttribute("title", "Welcome to TanishQ");
		return "welcome";
		
	}

	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title", "About Page");
		return "welcome";
		
	}

	//customer signup
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title", "Customer Signup Page");
		model.addAttribute("user", new User());
		return "signup";
		
	}

	@RequestMapping(value="/do_signup/{typeAcc}", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("typeAcc") String typeAcc, @RequestParam(value="agreement", defaultValue="false") boolean agreement, Model model, HttpSession session)
	{
		if(result.hasErrors())
		{
			System.out.println("Errors found-"+result);
			return "signup";
		}
		else
		{
			try
			{
				
				if(!agreement)
				{
					System.out.println("You have not agreed the terms and conditions");
					throw new Exception("You have not agreed the terms and conditions exc");
					
				}
				switch(typeAcc)
				{
					case "ROLE_CUSTOMER":
						System.out.println("user-"+user);
						Customer cust = new Customer();
						cust.setEmail(user.getEmail());
						cust.setPhone(user.getPhone());
						cust.setName(user.getName());
						cust.setPassword(passwordEncoder.encode(user.getPassword()));
						cust.setImageURL("default.png");
						cust.setDob(LocalDate.parse("0000-01-01"));
						cust.setRole(typeAcc);
						System.out.println("cust-"+cust);
					this.userRepository.save(cust);
					break;
				
					case "ROLE_SELLER": 	
						System.out.println("user-"+user);
						ShopOwner seller = new ShopOwner();
						seller.setEmail(user.getEmail());
						seller.setPhone(user.getPhone());
						seller.setName(user.getName());
						seller.setPassword(passwordEncoder.encode(user.getPassword()));
						seller.setImageURL("default.png");
						seller.setDob(LocalDate.parse("0000-01-01"));
						seller.setRole(typeAcc);
						System.out.println("seller-"+seller);
					this.sellerRepository.save(seller);
					break;
					
					case "ROLE_COURIER": 	
						System.out.println("user-"+user);
						CourierServiceProvider csp = new CourierServiceProvider();
						csp.setEmail(user.getEmail());
						csp.setPhone(user.getPhone());
						csp.setName(user.getName());
						csp.setPassword(passwordEncoder.encode(user.getPassword()));
						csp.setImageURL("default.png");
						csp.setDob(LocalDate.parse("0000-01-01"));
						csp.setRole(typeAcc);
						System.out.println("csp-"+csp);
					this.courierRepository.save(csp);
				}
				model.addAttribute("user", new User());
				session.setAttribute("message", new Message("Successfully registered", "alert-success"));
				return "signup";

			}
			catch (DataIntegrityViolationException emailException) 
			{
				System.out.println("emailException");
				session.setAttribute("message", new Message("Email already exists", "alert-danger"));
				return "signup";
				
			}
			catch (Exception e) 
			{
				System.out.println("e rr 1");
				e.printStackTrace();
				System.out.println("e rr 2");

				model.addAttribute("user",user);
				session.setAttribute("message", new Message("Something went wrong: "+e.getMessage(), "alert-danger"));
				return "signup";
			}
			
		}
		
	}
	
	
	//show order details
	@RequestMapping("/{oid}/order")
	public String showContactDetails(@PathVariable("oid") Long oid, Model model, Principal principal, HttpSession session)
	{System.out.println("inside order details");
		
		Optional<OrderDetails> orderDetailsOptional = this.orderRepository.findById(oid);
		OrderDetails orderDetails = orderDetailsOptional.get();
		//check for right user access for the contacts
		//User user=null;
		String userName = principal.getName();

		Customer user1 = this.userRepository.getUserByName(userName);
		//if not customer
		if(user1==null)
		{
			ShopOwner user2 = this.sellerRepository.getSellerByName(userName);
			//if not seller
			if(user2==null)
			{
				CourierServiceProvider user3 = this.courierRepository.getCourierByName(userName);
				if(user3.getId()==orderDetails.getCsp().getId()) 
				{
					System.out.println("courier matched");
					model.addAttribute("user", user3);
					model.addAttribute("orderDetails", orderDetails);
					model.addAttribute("list", orderDetails.getItemsList());
					System.out.println("Access granted to courier"+orderDetails.getItemsList());
					return "/order_details3";	
				}
				else
				{
					System.out.println("Access denied to courier");
					//session.setAttribute("message", new Message("You are not allowed to access this page", "warning"));
					return "redirect:/home";	
				}
				
			}
			else
			{
				if(user2.getId()==orderDetails.getShop().getShopOwner().getId()) 
				{
					System.out.println("seller matched");
					model.addAttribute("user", user2);
					model.addAttribute("orderDetails", orderDetails);
					model.addAttribute("list", orderDetails.getItemsList());
					System.out.println("Access granted to seller");
					return "/order_details";	
				}
				else
				{
					System.out.println("Access denied to seller");
					//session.setAttribute("message", new Message("You are not allowed to access this page", "warning"));
					return "redirect:/home";	
				}
			}
		}
		else
		{
			if(user1.getId()==orderDetails.getCustomer().getId()) 
			{
				System.out.println("customer matched");
				model.addAttribute("user", user1);
				model.addAttribute("orderDetails", orderDetails);
				model.addAttribute("list", orderDetails.getItemsList());
				System.out.println("Access granted to customer");
				return "/order_details2";	
			}
			else
			{
				System.out.println("Access denied to customer");
				//session.setAttribute("message", new Message("You are not allowed to access this page", "warning"));
				return "redirect:/home";	
			}
		}
			

	}
	
}
