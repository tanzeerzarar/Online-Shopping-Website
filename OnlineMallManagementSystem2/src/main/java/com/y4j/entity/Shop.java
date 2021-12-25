package com.y4j.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.y4j.enumm.ShopCategory;

@Entity
@Table
public class Shop 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long shopId;
	String shopName;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="owner_id", nullable=false)	
	ShopOwner shopOwner;
	
	@Column(name="categoryofshop")
	@Enumerated(EnumType.STRING)
	private ShopCategory shopCategory;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="mall_id", nullable=false)	
	Mall mall;	
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shop")
	List<Employee> shopEmployees;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shop")	
	List<Item> items;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shop")
	List<OrderDetails> orders;

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public ShopOwner getShopOwner() {
		return shopOwner;
	}

	public void setShopOwner(ShopOwner shopOwner) {
		this.shopOwner = shopOwner;
	}

	public ShopCategory getShopCategory() {
		return shopCategory;
	}

	public void setShopCategory(ShopCategory shopCategory) {
		this.shopCategory = shopCategory;
	}

	public Mall getMall() {
		return mall;
	}

	public void setMall(Mall mall) {
		this.mall = mall;
	}

	public List<Employee> getShopEmployees() {
		return shopEmployees;
	}

	public void setShopEmployees(List<Employee> shopEmployees) {
		this.shopEmployees = shopEmployees;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}


	
	
}
