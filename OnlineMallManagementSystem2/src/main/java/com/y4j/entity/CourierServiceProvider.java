package com.y4j.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
@DiscriminatorValue("ROLE_COURIER")

public class CourierServiceProvider extends User
{
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "csp")
	List<OrderDetails> orders = new ArrayList<OrderDetails>();

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "CourierServiceProvider [orders=" + orders + "]";
	};
	

	
}
	