package com.y4j.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.Customer;
import com.y4j.entity.Item;
import com.y4j.entity.Mall;

@Repository
public interface MallRepository extends JpaRepository<Mall, Long> {

	@Query("select cx from Mall cx where cx.mallName = :name")
	public Mall getUserByName(@Param("name") String name);



}
