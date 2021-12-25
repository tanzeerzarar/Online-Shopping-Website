package com.y4j.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.Shop;
import com.y4j.entity.ShopOwner;
import com.y4j.entity.User;

@Repository
public interface SellerRepository extends JpaRepository<ShopOwner, Long> {

	@Query("select sl from ShopOwner sl where sl.email = :email")
	public ShopOwner getSellerByName(@Param("email") String email);
	
//	@Query("select sl from ShopOwner sl where sl.email = :shopid")
//	public Shop getShopById(@Param("shopid") String shopid);

}
