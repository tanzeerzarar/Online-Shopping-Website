package com.y4j.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
@DiscriminatorValue("ROLE_SELLER")
public class ShopOwner extends User
{
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shopOwner")
	private List<Shop> shops;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shopOwner")
	private List<ShopRequest> shopRequest;
	
	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public List<ShopRequest> getShopRequest() {
		return shopRequest;
	}

	public void setShopRequest(List<ShopRequest> shopRequest) {
		this.shopRequest = shopRequest;
	}

//	@Override
//	public String toString() {
//		return "ShopOwner [shops=" + shops + ", shopRequest=" + shopRequest + ", role=" + role + ", dob=" + dob
//				+ ", getShops()=" + getShops() + ", getShopRequest()=" + getShopRequest() + ", getRole()=" + getRole()
//				+ ", getId()=" + getId() + ", getName()=" + getName() + ", getPassword()=" + getPassword()
//				+ ", getEmail()=" + getEmail() + ", getPhone()=" + getPhone() + ", getDob()=" + getDob()
//				+ ", getImageURL()=" + getImageURL() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
//				+ ", toString()=" + super.toString() + "]";
//	}

}
