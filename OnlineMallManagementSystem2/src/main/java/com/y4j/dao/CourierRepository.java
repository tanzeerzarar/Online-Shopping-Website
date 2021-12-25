package com.y4j.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.CourierServiceProvider;
import com.y4j.entity.OrderDetails;

@Repository
public interface CourierRepository extends JpaRepository<CourierServiceProvider, Long> {

	@Query("select sl from CourierServiceProvider sl where sl.email = :email")
	public CourierServiceProvider getCourierByName(@Param("email") String email);
	
	@Query("select od from OrderDetails od where od.csp.id = :cspid")
	public Page<OrderDetails> findOrderByCourierId(@Param("cspid") long cspid, Pageable pg);

}
