package com.y4j.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.CourierServiceProvider;
import com.y4j.entity.MallAdmin;
import com.y4j.entity.OrderDetails;
import com.y4j.entity.ShopRequest;

@Repository
public interface AdminRepository extends JpaRepository<MallAdmin, Long> {

	@Query("select sl from MallAdmin sl where sl.email = :email")
	public MallAdmin getAdminByName(@Param("email") String email);
	

}
