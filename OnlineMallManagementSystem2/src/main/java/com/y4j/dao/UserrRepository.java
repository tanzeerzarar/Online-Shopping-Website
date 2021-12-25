package com.y4j.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.y4j.entity.User;


@Repository
public interface UserrRepository extends JpaRepository<User, Long> {

	@Query("select cx from User cx where cx.email = :email")
	public User getUserByName(@Param("email") String email);

}
