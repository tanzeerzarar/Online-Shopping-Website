package com.y4j.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.Customer;


@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {

	@Query("select cx from Customer cx where cx.email = :email")
	public Customer getUserByName(@Param("email") String email);

}
