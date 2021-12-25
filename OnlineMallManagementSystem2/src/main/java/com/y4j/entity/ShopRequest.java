package com.y4j.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.y4j.enumm.RequestStatus;
import com.y4j.enumm.ShopCategory;

@Entity
@Table
public class ShopRequest 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	String shopname;
	
	@ManyToOne
	private ShopOwner shopOwner;
		
	@Enumerated(EnumType.STRING)
	private ShopCategory shopCategory;
	
	@Enumerated(EnumType.STRING)
	private RequestStatus requestStatus;
	
	@ManyToOne
	Mall mall;

	public ShopOwner getShopOwner() {
		return shopOwner;
	}

	public void setShopOwner(ShopOwner shopOwner) {
		this.shopOwner = shopOwner;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ShopRequest [id=" + id + ", shopOwner=" + shopOwner + ", shopCategory=" + shopCategory
				+ ", requestStatus=" + requestStatus + ", mall=" + mall + ", getShopOwner()=" + getShopOwner()
				+ ", getRequestStatus()=" + getRequestStatus() + ", getId()=" + getId() + ", getShopCategory()="
				+ getShopCategory() + ", getMall()=" + getMall() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}	
	
	
}
