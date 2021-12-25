package com.y4j.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
@DiscriminatorValue("ROLE_CUSTOMER")
public class Customer extends User
{
	String address;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
	List<OrderDetails> orders = new ArrayList<OrderDetails>();;
	
	@ManyToMany
	List<Item> cart = new ArrayList<Item>();
	
	public List<OrderDetails> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}
	public String getAddress() {
		return address;
	}
	public List<Item> getCart() {
		return cart;
	}
	public void setCart(List<Item> cart) {
		this.cart = cart;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	@Override
	public String toString() {
		return "Customer [address=" + address + ", orders=" + orders + ", dob=" + dob + ", getOrders()=" + getOrders()
				+ ", getAddress()=" + getAddress() + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getPassword()=" + getPassword() + ", getRoles()=" + getRole() + ", getEmail()=" + getEmail()
				+ ", getPhone()=" + getPhone() + ", getDob()=" + getDob() + ", getImageURL()=" + getImageURL()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	
}
	