package com.gscdn.managedapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gscdn.managedapi.model.entity.User;

public interface UserJpaRepo extends JpaRepository<User, Integer> {

}
