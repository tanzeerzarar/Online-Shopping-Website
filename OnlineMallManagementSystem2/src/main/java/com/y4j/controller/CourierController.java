package com.y4j.controller;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.y4j.dao.CourierRepository;
import com.y4j.dao.ItemRepository;
import com.y4j.dao.MallRepository;
import com.y4j.dao.OrderRepository;
import com.y4j.dao.RequestRepository;
import com.y4j.dao.SellerRepository;
import com.y4j.dao.ShopRepository;
import com.y4j.dao.UserRepository;
import com.y4j.dao.UserrRepository;
import com.y4j.entity.CourierServiceProvider;
import com.y4j.entity.Item;
import com.y4j.entity.Mall;
import com.y4j.entity.OrderDetails;
import com.y4j.entity.Shop;
import com.y4j.entity.ShopOwner;
import com.y4j.entity.ShopRequest;
import com.y4j.entity.User;
import com.y4j.enumm.ItemCategory;
import com.y4j.enumm.OrderStatus;
import com.y4j.enumm.RequestStatus;
import com.y4j.enumm.ShopCategory;
import com.y4j.helper.Message;


@Controller
@RequestMapping("/cr")
public class CourierController 
{	
	@Autowired
	CourierRepository courierRepository;

	@Autowired
	OrderRepository orderRepository;
			
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;	

	@Autowired
	UserrRepository userrRepository;
	//method to add common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();
		CourierServiceProvider user = courierRepository.getCourierByName(userName);
		System.out.println("in common- "+user.getName());
		System.out.println(user.getRole()+"---- "+user.getEmail());
		model.addAttribute("user", user);
		
	}
	// home dashboard
	@RequestMapping("/dashboard/{page}")
	public String dashboard(@PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("dashboard courier");

		String userName = principal.getName();
		CourierServiceProvider user = this.courierRepository.getCourierByName(userName);
		model.addAttribute("title", "Courier Dashboard");
		Pageable pageable = PageRequest.of(page, 6, Sort.by("id").descending());	
		Page<OrderDetails> orderList = this.courierRepository.findOrderByCourierId(user.getId(), pageable);
		model.addAttribute("items", orderList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orderList.getTotalPages());
		return "cr/cr_dashboard";
	}
	
	//profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{System.out.println("Inside profile");
		model.addAttribute("title", "Profile Page");
		return "/cr/profile";
	}

	//settings page
	@RequestMapping("/settings")
	public String settings(Model model, Principal principal)
	{System.out.println("inside settings page");
		
		model.addAttribute("title", "Settings page");
		//check for right user access for the contacts
		return "/cr/settings";		
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
		return "redirect:/cr/settings";
	}

	//order status update
	@GetMapping("/order-action/{orderid}")
	public String orderAction(@PathVariable("orderid") Long orderid, Model model, Principal principal, HttpSession session)
	{System.out.println("Inside order action");
	
		String userName = principal.getName();
		CourierServiceProvider user = this.courierRepository.getCourierByName(userName);
		OrderDetails od = this.orderRepository.getById(orderid);

		if(user.getId()==od.getCsp().getId())
		{
			System.out.println("courier approved-");
			if(od.getOrderStatus().equals(OrderStatus.SHIPPED))
				od.setOrderStatus(OrderStatus.ONTHEWAY);
			else
				od.setOrderStatus(OrderStatus.DELIVERED);
			this.orderRepository.save(od);
			
			session.setAttribute("message", new Message("Status of the order was changed", "success"));
		}
		else
		{
			System.out.println("seller not approved-");
			session.setAttribute("message", new Message("You are not authorized to take action on this order", "error"));
		}
		return "redirect:/cr/dashboard/0";
	}
	
}