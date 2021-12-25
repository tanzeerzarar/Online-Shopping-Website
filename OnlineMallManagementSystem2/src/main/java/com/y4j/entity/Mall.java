package com.y4j.entity;

import java.util.List;

import javax.persistence.CascadeType;
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

import com.y4j.enumm.MallCategory;

@Entity
@Table
public class Mall 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="admin_id", nullable=false)	
	MallAdmin mallAdmin;
	String mallName;
	String location;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mall")	
	List<Shop> shops;
	
	@Enumerated(EnumType.STRING)
	private MallCategory mallCategory;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mall")
	private List<ShopRequest> shopRequest;

	
	public MallCategory getMallCategory() {
		return mallCategory;
	}
	public void setMallCategory(MallCategory mallCategory) {
		this.mallCategory = mallCategory;
	}
	public List<ShopRequest> getShopRequest() {
		return shopRequest;
	}
	public void setShopRequest(List<ShopRequest> shopRequest) {
		this.shopRequest = shopRequest;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public MallAdmin getMallAdmin() {
		return mallAdmin;
	}
	public void setMallAdmin(MallAdmin mallAdmin) {
		this.mallAdmin = mallAdmin;
	}
	public String getMallName() {
		return mallName;
	}
	public void setMallName(String mallName) {
		this.mallName = mallName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Shop> getShops() {
		return shops;
	}
	public void setShops(List<Shop> shops) {
		this.shops = shops;
	};

	
}
