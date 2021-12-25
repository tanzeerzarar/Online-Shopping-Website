package com.y4j.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.Shop;
import com.y4j.entity.ShopOwner;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

	@Query("from Shop as c where c.shopId =:shopId")
	public Shop findById(@Param("shopId")long shopId);
	
	@Query("from Shop as c where c.shopOwner.id =:userId")
	public Page<Shop> findShopsById(@Param("userId")long userId, Pageable pg);

	

}
