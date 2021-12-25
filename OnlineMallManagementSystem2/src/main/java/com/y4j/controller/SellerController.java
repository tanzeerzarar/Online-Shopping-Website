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
import com.y4j.dao.EmployeeRepository;
import com.y4j.dao.ItemRepository;
import com.y4j.dao.MallRepository;
import com.y4j.dao.OrderRepository;
import com.y4j.dao.RequestRepository;
import com.y4j.dao.SellerRepository;
import com.y4j.dao.ShopRepository;
import com.y4j.dao.UserRepository;
import com.y4j.dao.UserrRepository;
import com.y4j.entity.CourierServiceProvider;
import com.y4j.entity.Employee;
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
@RequestMapping("/sl")
public class SellerController 
{	
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;	

	@Autowired
	UserrRepository userrRepository;
	
	@Autowired
	CourierRepository courierRepository;

	@Autowired
	MallRepository mallRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	SellerRepository sellerRepository;
		
	@Autowired
	OrderRepository orderRepository;
		
	@Autowired
	ShopRepository shopRepository;
	
	//method to add common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();

		//System.out.println(userName);
		//get the user using userName
		ShopOwner user = sellerRepository.getSellerByName(userName);
		//System.out.println("in common- "+user.getName());
		//System.out.println(user.getRoles());
		//System.out.println("in common- "+user.getEmail());
		model.addAttribute("user", user);
		
	}
	// home dashboard
	@RequestMapping("/dashboard/{page}")
	public String dashboard(@PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("dashboard seller");

		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		model.addAttribute("title", "Seller Dashboard");
		Pageable pageable = PageRequest.of(page, 6, Sort.by("dateOfPurchase").descending());	
		//Page<Item> items = this.itemRepository.findAll(pageable);
		Page<OrderDetails> orderList = this.orderRepository.findOrderBySeller(user.getId(), pageable);
//		List<Item> itemList = items.getContent();/only 5 items
		//System.out.println("shop-"+orderList.getContent());
		model.addAttribute("items", orderList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orderList.getTotalPages());

		
		return "sl/sl_dashboard";
	}
	
	// shop requests
	@RequestMapping("/show-requests/{page}")
	public String showRequest(@PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("inside show request handler");

		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		
		Pageable pageable = PageRequest.of(page, 6, Sort.by("id").descending());	
		//Page<Item> items = this.itemRepository.findAll(pageable);
		Page<ShopRequest> shopRequest = this.requestRepository.findRequestsBySeller(user.getId(), pageable);
////		List<Item> itemList = items.getContent();/only 5 items
		
//		//System.out.println("shop-"+orderList.getContent());
		model.addAttribute("title", "Shop requests");
		model.addAttribute("shopRequest", shopRequest);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", shopRequest.getTotalPages());

		
		return "sl/show_requests";
	}

	// shop requests apply form
	@RequestMapping("/{sellerid}/requestshop")
	public String requestShop(@PathVariable("sellerid") Long sellerid, Model model, Principal principal)
	{System.out.println("inside shop new request handler");

		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		
		model.addAttribute("title", "Make new request");
		
		return "sl/add_request";
	}
	
	// process shop request
	@RequestMapping("process-request/{shopc}/{mall}/{shopname}")
	public String processRequestShop(HttpSession session, @RequestParam("mall") String mallname, @RequestParam("shopname") String shopname, @RequestParam("shopc") String shopc, Model model, Principal principal)
	{System.out.println("inside process request");

		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		
		ShopRequest shopRequest = new ShopRequest();
		shopRequest.setShopOwner(user);
		shopRequest.setRequestStatus(RequestStatus.PENDING);
		Mall mall = this.mallRepository.getUserByName(mallname);
		shopRequest.setMall(mall);
		shopRequest.setShopname(shopname);
		switch(shopc)
		{
			case "ELECTRICAL": shopRequest.setShopCategory(ShopCategory.ELECTRICAL); break;
			case "ELECTRONICS": shopRequest.setShopCategory(ShopCategory.ELECTRONICS); break;
			case "GROCERY": shopRequest.setShopCategory(ShopCategory.GROCERY); break;
			case "CROCKERY": shopRequest.setShopCategory(ShopCategory.CROCKERY);
		}
		System.out.println("sr-"+shopRequest);
		this.requestRepository.save(shopRequest);
		session.setAttribute("message", new Message("Your request for shop has been sent. Please check your account for the update.", "success"));
		return "redirect:/sl/dashboard/0";
	}
	
	// manage shops - list
	@RequestMapping("/show-shops/{page}")
	public String manageShops( @PathVariable("page") int page, Model model, Principal principal)
	{System.out.println("inside show/manage shops");
	
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);	
		Pageable pageable = PageRequest.of(page, 5);	
		System.out.println("user-"+user.getName());
		Page<Shop> shopPage = this.shopRepository.findShopsById(user.getId(), pageable);

		//List<Shop> listShop = this.shopRepository.findAll();
		System.out.println("shopList-"+shopPage);
		model.addAttribute("title", "Manage Your Shops");
		model.addAttribute("items", shopPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", shopPage.getTotalPages());

		return "sl/show_shops";
	}
	
	// manage ITEMS - list
	@RequestMapping("/{shopId}/shopitems/{page}")
	public String manageItems(@PathVariable("shopId") Long shopId, @PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("inside manage items");
	
		//check if the user is correct as the link shop id
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		Shop shop = this.shopRepository.getById(shopId);
		if(user.getId()==shop.getShopOwner().getId())
		{
			System.out.println("seller approved-");
			Pageable pageable = PageRequest.of(page, 6);	
			Page<Item> items = this.itemRepository.findItemsByShopId(shopId, pageable);
			System.out.println("itemList-"+items);
			model.addAttribute("title", "Manage Your Items");
			model.addAttribute("items", items);	
			model.addAttribute("shopid", shop.getShopId());	
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", items.getTotalPages());

			return "sl/show_shop_items";

		}
		else
		{
			System.out.println("seller not approved-");
			return "sl/show_shops";
		}
	}

	// manage employees - list
	@RequestMapping("/{shopId}/shop-employees/{page}")
	public String manageEmployees(@PathVariable("shopId") Long shopId, @PathVariable("page") Integer page, Model model, Principal principal)
	{System.out.println("inside manage employees");
	
		//check if the user is correct as the link shop id
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		Shop shop = this.shopRepository.getById(shopId);
		if(user.getId()==shop.getShopOwner().getId())
		{
			System.out.println("seller approved-");
			Pageable pageable = PageRequest.of(page, 6);	
			Page<Employee> employees = this.employeeRepository.findEmployeesByShopId(shopId, pageable);
			System.out.println("itemList-"+employees);
			model.addAttribute("title", "Manage Your Employees");
			model.addAttribute("items", employees);	
			model.addAttribute("shopid", shop.getShopId());	
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", employees.getTotalPages());

			return "sl/show_shop_employees";

		}
		else
		{
			System.out.println("seller not approved-");
			return "sl/show_shops";
		}
	}
	
	//order status update
	@GetMapping("/order-action/{orderid}")
	public String orderAction(@PathVariable("orderid") Long orderid, Model model, Principal principal, HttpSession session)
	{System.out.println("Inside order action");
	
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		OrderDetails od = this.orderRepository.getById(orderid);

		if(user.getId()==od.getShop().getShopOwner().getId())
		{
			System.out.println("seller approved-");
			if(od.getOrderStatus().equals(OrderStatus.PLACED))
				od.setOrderStatus(OrderStatus.PACKED);
			else
			{
				od.setOrderStatus(OrderStatus.SHIPPED);
				//fetch courier partner and update in order details - hard code 98
//				Optional<CourierServiceProvider> cspo = this.courierRepository.findById((long) 98);
//				CourierServiceProvider csp = cspo.get();
//				od.setCsp(csp);
			}
			this.orderRepository.save(od);
			
			session.setAttribute("message", new Message("Status of the order was changed", "success"));
		}
		else
		{
			System.out.println("seller not approved-");
			session.setAttribute("message", new Message("You are not authorized to take action on this order", "error"));
		}
		return "redirect:/sl/dashboard/0";
	}

	//add item to shop 
	@GetMapping("/{sellerid}/additem/{shopid}")
	public String addItemToShop(@PathVariable("sellerid") Long sellerid, @PathVariable("shopid") Long shopid, Model model, Principal principal, HttpSession session)
	{System.out.println("Inside add item to shop-");
	
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		//OrderDetails od = this.orderRepository.getById(orderid);

		if(user.getId()==sellerid)
		{
			System.out.println("seller approved-+"+shopid);
			
			Item item = new Item();
			Optional<Shop> shopOptional = this.shopRepository.findById(shopid);
			Shop shop = shopOptional.get();
			item.setShop(shop);
			model.addAttribute("title", "Add item");
			model.addAttribute("contact", item);
			model.addAttribute("shopid", shopid);
			System.out.println("shopid of item-+"+item.getShop().getShopId());
			return "sl/add_item";
		}
		else
		{
			System.out.println("seller not approved-");
			session.setAttribute("message", new Message("You are not authorized to add item for this shop", "error"));
			return "redirect:/sl/dashboard/0";
		}
		
	}						
	//add employee to shop 
	@GetMapping("/{sellerid}/add-employee/{shopid}")
	public String addEmloyeeToShop(@PathVariable("sellerid") Long sellerid, @PathVariable("shopid") Long shopid, Model model, Principal principal, HttpSession session)
	{System.out.println("Inside add employee to shop-");
	
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		//OrderDetails od = this.orderRepository.getById(orderid);

		if(user.getId()==sellerid)
		{
			System.out.println("seller approved-+"+shopid);
			
			Employee employee = new Employee();
			Optional<Shop> shopOptional = this.shopRepository.findById(shopid);
			Shop shop = shopOptional.get();
			employee.setShop(shop);
			model.addAttribute("title", "Add employee");
			model.addAttribute("contact", employee);
			model.addAttribute("shopid", shopid);
			System.out.println("shopid of item-+"+employee.getShop().getShopId());
			return "sl/add_employee";
		}
		else
		{
			System.out.println("seller not approved-");
			session.setAttribute("message", new Message("You are not authorized to add item for this shop", "error"));
			return "redirect:/sl/dashboard/0";
		}
		
	}						

	//add item form update 
	@PostMapping("/process-additem/{itemc}/{mdate}/{edate}")
	public String processContact(@ModelAttribute Item item, @RequestParam("mdate") String mdate, @RequestParam("edate") String edate, @RequestParam("itemc") String category, Principal principal, HttpSession session, Model model)
	{System.out.println("inside add item form-"+category);
		try 
		{
			String name = principal.getName();
			ShopOwner user = this.sellerRepository.getSellerByName(name);

			switch(category)
			{
				case "ELECTRICAL": item.setItemCategory(ItemCategory.ELECTRICAL); break;
				case "ELECTRONICS": item.setItemCategory(ItemCategory.ELECTRONICS); break;
				case "GROCERY": item.setItemCategory(ItemCategory.GROCERY); break;
				case "CROCKERY": item.setItemCategory(ItemCategory.CROCKERY); break;
				case "ACCESSORIES": item.setItemCategory(ItemCategory.ACCESSORIES); break;
				case "OTHERS": item.setItemCategory(ItemCategory.OTHERS);
			}
			if(mdate=="")
				mdate="0000-01-01";
			if(edate=="")
				edate="0000-01-01";
			System.out.println(mdate+"-"+edate);
			item.setManufacturing(LocalDate.parse(mdate));
			item.setExpiry(LocalDate.parse(edate));

			System.out.println("User "+user);
			System.out.println("Item name "+item.getName());
			System.out.println("Item price "+item.getPrice());
			System.out.println("Item cat "+item.getItemCategory());
			System.out.println("mdate "+item.getManufacturing());
			System.out.println("edate "+item.getExpiry());
	
			
			System.out.println("everything seems perfect-"+item.getShop().getShopId());
			//message success
			this.itemRepository.save(item);
			session.setAttribute("message", new Message("Your item has been added. Add more!", "success"));
			
		}
		catch(Exception e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
//			session.setAttribute("message", new Message("Something went wrong, try again!", "danger"));
		}
		
		Item item2 = new Item();
		Shop shop = this.shopRepository.findById(item.getShop().getShopId());
		item2.setShop(shop);
		model.addAttribute("title", "Add item");
		model.addAttribute("contact", item2);
		model.addAttribute("shopid", item.getShop().getShopId());
		System.out.println("shopid of itemssssssss-+"+item.getShop().getShopId());
		return "sl/add_item";
	}
	//add employee form update 
	@PostMapping("/process-add-employee/{dob1}")
	public String processAddEmployee(@ModelAttribute Employee employee, @RequestParam("dob1") String dob,  Principal principal, HttpSession session, Model model)
	{System.out.println("inside add employee form-");
		try 
		{
			String name = principal.getName();
			ShopOwner user = this.sellerRepository.getSellerByName(name);
			employee.setDob(LocalDate.parse(dob));

			System.out.println("User "+user);
			System.out.println("employee- "+employee);

	
			
			System.out.println("everything seems perfect-");
			//message success
			this.employeeRepository.save(employee);
			session.setAttribute("message", new Message("Your employee has been added. Add more!", "success"));
			
		}
		catch(Exception e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong, try again!", "danger"));
		}
		
		Employee emp = new Employee();
		Shop shop = this.shopRepository.findById(employee.getShop().getShopId());
		emp.setShop(shop);
		model.addAttribute("title", "Add Employee");
		model.addAttribute("contact", emp);
		model.addAttribute("shopid", shop.getShopId());
		System.out.println("shopid of employee-+"+shop.getShopId());
		return "sl/add_employee";
	}


	//profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{System.out.println("Inside profile");
		model.addAttribute("title", "Profile Page");
		return "/sl/profile";
	}

	//settings page
	@RequestMapping("/settings")
	public String settings(Model model, Principal principal)
	{System.out.println("inside settings page");
		
		model.addAttribute("title", "Settings page");
		//check for right user access for the contacts
		return "/sl/settings";		
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
		return "redirect:/sl/settings";
	}

	//remove item from shop
	@GetMapping("/remove/{iid}")
	public String removeItem(@PathVariable("iid") Long iid, Model model, Principal principal, HttpSession session)
	{System.out.println("inside remove item-");
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		Optional<Item> optionalItem = this.itemRepository.findById(iid);
		Item item = optionalItem.get();
		Shop shop=item.getShop();
		System.out.println("shop is-"+shop.getShopId());
		//Shop shop = this.shopRepository.findById(item.getShop().getShopId());
		if(item.getShop().getShopOwner().getId()==user.getId())
		{System.out.println("seller-approved-"+item.getId());
			this.itemRepository.delete(item); //dont remove item from database // instead set shopid 0
			
//			item.setShop(null);
//			this.itemRepository.save(item);
			Pageable pageable = PageRequest.of(0, 6);	
			Page<Item> items = this.itemRepository.findItemsByShopId(shop.getShopId(), pageable);
			//System.out.println("itemList-"+items);
			model.addAttribute("title", "Manage Your Items");
			model.addAttribute("items", items);	
			model.addAttribute("shopid", shop.getShopId());	
			model.addAttribute("currentPage", 0);
			model.addAttribute("totalPages", items.getTotalPages());
			session.setAttribute("message", new Message("The item was removed from the shop successfully", "success"));
			return "sl/show_shop_items";

		}
		else
		{
			System.out.println("seller-denied-");
			return "redirect:/sl/dashboard/0";
		}		
		//return "redirect:/sl/dashboard/0";

	}
	//remove employee from shop
	@GetMapping("/remove-employee/{eid}")
	public String removeEmployee(@PathVariable("eid") Long eid, Model model, Principal principal, HttpSession session)
	{System.out.println("inside remove employee-");
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		System.out.println("seller is-"+user.getId());
		Optional<Employee> optionalItem = this.employeeRepository.findById(eid);
		Employee emp = optionalItem.get();
		System.out.println("emp is-"+emp.getId());
		Shop shop=emp.getShop();
		System.out.println("shop is-"+shop.getShopId());
		//Shop shop = this.shopRepository.findById(item.getShop().getShopId());
		if(emp.getShop().getShopOwner().getId()==user.getId())
		{System.out.println("seller-approved-");
			this.employeeRepository.delete(emp);
			
			Pageable pageable = PageRequest.of(0, 6);	
			Page<Employee> empPage = this.employeeRepository.findEmployeesByShopId(shop.getShopId(), pageable);
			//System.out.println("itemList-"+items);
			model.addAttribute("title", "Manage Your Employees");
			model.addAttribute("items", empPage);	
			model.addAttribute("shopid", shop.getShopId());	
			model.addAttribute("currentPage", 0);
			model.addAttribute("totalPages", empPage.getTotalPages());
			session.setAttribute("message", new Message("The employee was removed from the shop successfully", "success"));
			return "sl/show_shop_employees";

		}
		else
		{
			System.out.println("seller-denied-");
			return "redirect:/sl/dashboard/0";
		}		

	}
	//remove shop
	@GetMapping("/remove-shop/{sid}")
	public String removeShop(@PathVariable("sid") Long sid, Model model, Principal principal, HttpSession session)
	{System.out.println("inside remove shop-");
		String userName = principal.getName();
		ShopOwner user = this.sellerRepository.getSellerByName(userName);
		
		
		Optional<Shop> optionalShop = this.shopRepository.findById(sid);
		Shop shop = optionalShop.get();
//		Shop shop=item.getShop();
		System.out.println("shop is-"+shop.getShopOwner().getId());
		//Shop shop = this.shopRepository.findById(item.getShop().getShopId());
		if(shop.getShopOwner().getId()==user.getId())
		{System.out.println("seller-approved-"+user.getId());
			
			this.shopRepository.delete(shop);
			
			Pageable pageable = PageRequest.of(0, 5);	
			//System.out.println("user-"+user.getName());
			Page<Shop> shopPage = this.shopRepository.findShopsById(user.getId(), pageable);

			//List<Shop> listShop = this.shopRepository.findAll();
			//System.out.println("shopList-"+shopPage);
			model.addAttribute("title", "Manage Your Shops");
			model.addAttribute("items", shopPage);	
			model.addAttribute("currentPage", 0);
			model.addAttribute("totalPages", shopPage.getTotalPages());
			session.setAttribute("message", new Message("The shop was closed successfully", "success"));
			return "sl/show_shops";

		}
		else
		{
			System.out.println("seller-denied-");
			return "redirect:/sl/dashboard/0";
		}		

	}

	//open update form
	@PostMapping("/update-seller")
	public String updateSeller(Model model, Principal principal)
	{System.out.println("Inside update-seller handler");
		model.addAttribute("title", "Update Account Details");

		ShopOwner user = this.sellerRepository.getSellerByName(principal.getName());
		System.out.println("seller "+user.getName());
		model.addAttribute("customer", user);
		return "sl/update_form";
	}

		//process update seller
	@RequestMapping(value="/process-update", method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute("shopOwner") ShopOwner shopOwner, @RequestParam("dob1") String dob1, @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session, Principal principal)
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
			{System.out.println("else");
			System.out.println("dob in else "+dob1);	
				shopOwner.setDob(LocalDate.parse(dob1));  
			}
			System.out.println("dob-"+shopOwner.getDob());

			Optional<ShopOwner> customerOptional = this.sellerRepository.findById(shopOwner.getId());
			ShopOwner shopOwner2 = customerOptional.get();
			//System.out.println("olduser-"+contact2);
			//System.out.println("newuser-"+customer);

			if(shopOwner.getName().equals(shopOwner2.getName()));
			else {System.out.println("name changed");
			shopOwner2.setName(shopOwner.getName());
			}
			if(shopOwner.getEmail().equals(shopOwner2.getEmail()));
			else {System.out.println("email changed");
			shopOwner2.setEmail(shopOwner.getEmail());
			}
			if(shopOwner.getPhone()==shopOwner2.getPhone());
			else {System.out.println("phone changed");
			shopOwner2.setPhone(shopOwner.getPhone());
			}
			if(dob1.equals(shopOwner2.getDob()));
			else {System.out.println("dob changed");
			shopOwner2.setDob(LocalDate.parse(dob1));
			}

			if(!file.isEmpty())
			{
				//file work rewrite
				
				//delete
				File deleteFile = new ClassPathResource("static/img").getFile();
				File deleteFile2 = new File(deleteFile, shopOwner2.getImageURL());
				deleteFile2.delete();
				
				//update
				File imageFile = new ClassPathResource("static/img").getFile();
				Path path =  Paths.get(imageFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				shopOwner2.setImageURL(file.getOriginalFilename());
				
			}
			System.out.println("newuser-"+shopOwner2);
			this.sellerRepository.save(shopOwner2);
			model.addAttribute("user", shopOwner2);
			session.setAttribute("message", new Message("Changes saved to your account", "success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return "redirect:/sl/profile";
	}
	
}