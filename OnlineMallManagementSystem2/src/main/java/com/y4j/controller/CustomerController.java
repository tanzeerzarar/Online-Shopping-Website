package com.y4j.controller;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.y4j.dao.OrderRepository;
import com.y4j.dao.SellerRepository;
import com.y4j.dao.ShopRepository;
import com.y4j.dao.UserRepository;
import com.y4j.dao.UserrRepository;
import com.y4j.entity.CourierServiceProvider;
import com.y4j.entity.Customer;
import com.y4j.entity.Item;
import com.y4j.entity.OrderDetails;
import com.y4j.entity.Shop;
import com.y4j.entity.User;
import com.y4j.enumm.OrderStatus;
import com.y4j.enumm.PaymentType;
import com.y4j.helper.Message;

@Controller
@RequestMapping("/cx")
public class CustomerController 
{
	@Autowired
	ShopRepository shopRepository;	

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;	
	
	@Autowired
	CourierRepository courierRepository;	
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserrRepository userrRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	SellerRepository sellerRepository;
		
	@Autowired
	OrderRepository orderRepository;
		
	//method to add common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();

		//System.out.println(userName);
		//get the user using userName
		Customer user = userRepository.getUserByName(userName);
		System.out.println("in common- "+user.getName());
		System.out.println(user.getRole());
		System.out.println("in common- "+user.getEmail());
		model.addAttribute("user", user);
		
	}
	// home dashboard
	@RequestMapping("/dashboard/{page}")
	public String dashboard(@PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("dashboard");

		model.addAttribute("title", "User Dashboard");
		Pageable pageable = PageRequest.of(page, 10);	
		Page<Item> items = this.itemRepository.findAll(pageable);
//		List<Item> itemList = items.getContent();/only 5 items
//		System.out.println("shop-"+itemList);
		model.addAttribute("items", items);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", items.getTotalPages());

		
		return "cx/cx_dashboard";
	}
	
	//profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{System.out.println("Inside profile");
		model.addAttribute("title", "Profile Page");
		return "/cx/profile";
	}
	
	//open update form
	@PostMapping("/update-contact")
	public String updateForm(Model model, Principal principal)
	{System.out.println("Inside update-account handler");
		model.addAttribute("title", "Update Contact");
		Customer customer = this.userRepository.getUserByName(principal.getName());
		System.out.println("cus "+customer);
		model.addAttribute("customer", customer);
		return "cx/update_form";
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
		return "redirect:/cx/settings";
	}
	
	
		//update contact
	@RequestMapping(value="/process-update", method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute("customer") Customer customer, @RequestParam("dob1") String dob1, @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session, Principal principal)
	{System.out.println("Inside process-update");
		try
		{	//System.out.println("newuser-"+customer);
			//System.out.println("String dob-"+dob1);
			if(dob1=="")
			{
				System.out.println("blank");
			}
			else if(dob1==null)
			{
				System.out.println("null");
			}
			else if(dob1==" ")
			{
				System.out.println("space");
			}
			else if(dob1=="          ")
			{
				System.out.println("blank date");
			}
			else
			{//System.out.println("else");
			//System.out.println("dob in else "+dob1);	
				customer.setDob(LocalDate.parse(dob1));  
			}
			//System.out.println("dob-"+customer.getDob());

			Optional<Customer> customerOptional = this.userRepository.findById(customer.getId());
			Customer contact2 = customerOptional.get();
			//System.out.println("olduser-"+contact2);
			//System.out.println("newuser-"+customer);

			if(customer.getName().equals(contact2.getName()));
			else {System.out.println("name changed");
				contact2.setName(customer.getName());
			}
			if(customer.getEmail().equals(contact2.getEmail()));
			else {System.out.println("email changed");
				contact2.setEmail(customer.getEmail());
			}
			if(customer.getPhone()==contact2.getPhone());
			else {System.out.println("phone changed");
				contact2.setPhone(customer.getPhone());
			}
			if(customer.getAddress().equals(contact2.getAddress()));
			else {System.out.println("address changed");
				contact2.setAddress(customer.getAddress());
			}
			if(dob1.equals(contact2.getDob()));
			else {System.out.println("dob changed");
				contact2.setDob(LocalDate.parse(dob1));
			}
			//System.out.println("olduser-"+contact2);
			//System.out.println("newuser-"+customer);

			if(!file.isEmpty())
			{
				//file work rewrite
				
				//delete
				File deleteFile = new ClassPathResource("static/img").getFile();
				File deleteFile2 = new File(deleteFile, contact2.getImageURL());
				deleteFile2.delete();
				
				//update
				File imageFile = new ClassPathResource("static/img").getFile();
				Path path =  Paths.get(imageFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact2.setImageURL(file.getOriginalFilename());
				
			}
			System.out.println("newuser-"+contact2);
			//Customer user=this.userRepository.getUserByName(principal.getName());
			//this.userRepository.delete(contact2);
			//contact.setId(contact2.getId());
			this.userRepository.save(contact2);
			model.addAttribute("user", contact2);
			session.setAttribute("message", new Message("Changes saved to your account", "success"));
		
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return "redirect:/cx/profile";
	}
	//show contacts
	@GetMapping("/show-orders/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("inside show orders");
	
		model.addAttribute("title", "Order History");
	
		String userName = principal.getName();
		//System.out.println("name-"+userName);
		Customer user = this.userRepository.getUserByName(userName);
		//System.out.println("id-"+user.getId());
		Pageable pageable = PageRequest.of(page, 8,  Sort.by("dateOfPurchase").descending());	
		Page<OrderDetails> orderDetails = this.orderRepository.findOrderByCustomer(user.getId(), pageable);
		System.out.println("content-"+orderDetails.getContent());
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orderDetails.getTotalPages());
		
		return "cx/show_orders";
	}

	//show each item
	@RequestMapping("/{iid}/item")
	public String itemDetails(@PathVariable("iid") Long iid, Model model, Principal principal)
	{System.out.println("inside item details");
		
		Optional<Item> itemsOptional = this.itemRepository.findById(iid);
		Item itemDetails = itemsOptional.get();
		model.addAttribute("orderDetails", itemDetails);
		//check for right user access for the contacts
		return "/cx/item_details";		
	}
	
	//settings page
	@RequestMapping("/settings")
	public String settings(Model model, Principal principal)
	{System.out.println("inside settings page");
		
		model.addAttribute("title", "Settings page");
		//check for right user access for the contacts
		return "/cx/settings";		
	}
	@RequestMapping("/update-cart/{item_id}")
	public String updateCart(@PathVariable("item_id") Long iid, Model model, Principal principal, HttpSession session)
	{System.out.println("inside update cart handler");
		
		String userName = principal.getName();
		Customer user = this.userRepository.getUserByName(userName);
		Item itemToAdd = this.itemRepository.getById(iid);
		System.out.println("cus-"+user.getName());
		System.out.println("b-cart-"+user.getCart());
		
		//check if shop is same
		if(user.getCart().size()>0)
		{
			if(user.getCart().get(0).getShop()==itemToAdd.getShop())
			{

				user.getCart().add(itemToAdd);
				System.out.println("a-cart-"+user.getCart());
				this.userRepository.save(user);
				session.setAttribute("message", new Message("The item has been added to the cart", "success"));
				
			}
			else {
				session.setAttribute("message", new Message("Items with different shops can't be added to cart. Please choose items from same shop or empty the cart first.", "danger"));

			}
			
		}
		else {
			user.getCart().add(itemToAdd);
			System.out.println("a-cart-"+user.getCart());
			this.userRepository.save(user);
			session.setAttribute("message", new Message("The item has been added to the cart", "success"));
			
		}
		
		return "redirect:/cx/show-cart";		
	}
	
	
	//show cart list
	@GetMapping("/show-cart")
	public String showCart( Model model, Principal principal)
	{System.out.println("inside show cart");
	
		model.addAttribute("title", "Your cart");
		String userName = principal.getName();
		Customer user = this.userRepository.getUserByName(userName);
		//System.out.println("list-"+user.getName());
		//System.out.println("list-"+user.getCart());
		int total=0;
		for(Item obj:user.getCart())
		{//System.out.println("list-"+obj);
			total+=obj.getPrice();
		}
		model.addAttribute("itemList", user.getCart());
		model.addAttribute("total", total);		
		return "/cx/show_cart";
	}

	//remove item from cart
	@GetMapping("/remove/{iid}")
	public String deleteContact(@PathVariable("iid") Long iid, Model model, Principal principal, HttpSession session)
	{
		String userName = principal.getName();
		Customer user = this.userRepository.getUserByName(userName);
		Optional<Item> optionalItem = this.itemRepository.findById(iid);
		Item item = optionalItem.get();
		//System.out.println("before-"+user.getItemsList());
		user.getCart().remove(item);
		//System.out.println("after-"+user.getItemsList());
		this.userRepository.save(user);
		session.setAttribute("message", new Message("The item was removed from cart successfully", "success"));
		return "redirect:/cx/show-cart";
	}
	//pre-checkout
	@GetMapping("/pre-checkout/{cid}")
	public String preChecout(@PathVariable("cid") Long cid, Model model, Principal principal, HttpSession session)
	{System.out.println("inside pre-checkout");
		Optional<Customer> optionalCustomer = this.userRepository.findById(cid);
		Customer customer = optionalCustomer.get();
		int total=0;
		for(Item obj:customer.getCart())
		{
			total+=obj.getPrice();
		}
		if(total==0)
		{
			session.setAttribute("message", new Message("Your cart is empty", "warning"));
			model.addAttribute("total", total);
			return "/cx/show_cart";
		}
		else
		{
			model.addAttribute("title", "Pre-Checkout");
			model.addAttribute("total", total);		
			model.addAttribute("itemList", customer.getCart());
			//System.out.println("out pre-checkout");
			return "/cx/pre_checkout";
		}
	}
	//pre-checkout
	@GetMapping("/place-order/{pm}")
	public String placeOrder(@PathVariable("pm") Long mode, Model model, Principal principal, HttpSession session)
	{System.out.println("inside place-order");
		String userName = principal.getName();
		Customer user = this.userRepository.getUserByName(userName);
		int total=0;
		for(Item obj:user.getCart())
		{
			total+=obj.getPrice();
		}
		Optional<CourierServiceProvider> cspo = this.courierRepository.findById((long) 98);
		CourierServiceProvider csp = cspo.get();
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCustomer(user);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
		String strDate = LocalDateTime.now().format(format); 
		orderDetails.setDateOfPurchase(LocalDateTime.parse(strDate, format));
		orderDetails.setOrderStatus(OrderStatus.PLACED);
		orderDetails.setShop(user.getCart().get(0).getShop());
		orderDetails.setCsp(csp);
		orderDetails.setTotal(total);
		if(mode==0)
			orderDetails.setPaymentType(PaymentType.ONLINE);
		else
			orderDetails.setPaymentType(PaymentType.CASH);
//		System.out.println("1u "+user.getCart());		
//		System.out.println("1o "+orderDetails.getItemsList());
		
		for(Item obj:user.getCart())
		{//System.out.println("a-");
			orderDetails.getItemsList().add(obj);
		}
		
		//orderDetails.setItemsList(user.getItemsList());
		//System.out.println("2o "+orderDetails.getItemsList());
		
		user.getCart().clear();
//		System.out.println("2u "+user.getCart());
//		System.out.println("2o "+orderDetails.getItemsList());
		this.orderRepository.save(orderDetails);
		//System.out.println("2");
		
		//System.out.println("3");
		model.addAttribute("title", "Order Placed");
		model.addAttribute("od", orderDetails);		
		model.addAttribute("list", orderDetails.getItemsList());	
		//System.out.println("out place order");
		session.setAttribute("message", new Message("Your order has been placed successfully", "success"));
		return "/cx/order_placed";
	}
	
	//show each seller
	@RequestMapping("/{sid}/seller")
	public String sellerDetails(@PathVariable("sid") Long sid, Model model, Principal principal)
	{System.out.println("inside seller details");
		
		Optional<Shop> sellerOptional = this.shopRepository.findById(sid);
		Shop seller = sellerOptional.get();
		model.addAttribute("seller", seller);
		//check for right user access for the contacts
		return "/cx/seller_details";		
	}

}
