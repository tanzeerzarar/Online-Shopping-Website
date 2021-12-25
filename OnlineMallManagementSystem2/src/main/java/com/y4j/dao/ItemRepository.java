package com.y4j.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query("from Item as c where c.id =:cartId")
	public Item findById(@Param("cartId")long cartId);
	
	@Query("from Item as c where c.shop.shopId =:shopId")
	public Page<Item> findItemsByShopId(@Param("shopId")long shopId, Pageable pg);
	


}
