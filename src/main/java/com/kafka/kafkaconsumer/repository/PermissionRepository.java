package com.kafka.kafkaconsumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kafka.kafkaconsumer.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
//	Optional<Permission> findByName(String name);
}
