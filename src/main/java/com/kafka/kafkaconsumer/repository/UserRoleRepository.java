package com.kafka.kafkaconsumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kafka.kafkaconsumer.model.UserRole;
import com.kafka.kafkaconsumer.model.UserRoleId;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    // Add methods to retrieve user roles as needed
}

