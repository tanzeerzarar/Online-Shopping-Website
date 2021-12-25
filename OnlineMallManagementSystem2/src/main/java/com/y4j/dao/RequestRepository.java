package com.y4j.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.OrderDetails;
import com.y4j.entity.ShopRequest;

@Repository
public interface RequestRepository extends JpaRepository<ShopRequest, Long> {

	//pagination
	
	@Query("from ShopRequest as c where c.shopOwner.id =:userId")
	public Page<ShopRequest> findRequestsBySeller(@Param("userId")long userId, Pageable pg);

	@Query("select od from ShopRequest od where od.mall.mallAdmin.id = :userId")
	public Page<ShopRequest> findRequestByAdminId(@Param("userId") long userId, Pageable pg);

	
//	@Query("from OrderDetails as c where c.shop.shopOwner.id =:userId")
//	public Page<OrderDetails> findOrderBySeller(@Param("userId")long userId, Pageable pg);


}
