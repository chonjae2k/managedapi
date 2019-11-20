package com.gscdn.managedapi.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gscdn.managedapi.model.entity.Distribution;

public interface DistributionJpaRepo extends JpaRepository<Distribution, Long> {
	List<Distribution> findByUid(String uid);
}
