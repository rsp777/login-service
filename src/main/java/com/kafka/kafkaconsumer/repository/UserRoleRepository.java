package com.kafka.kafkaconsumer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kafka.kafkaconsumer.model.UserRole;
import com.kafka.kafkaconsumer.model.UserRoleId;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole,UserRoleId> {
	
	@Query("SELECT ur FROM UserRole ur WHERE ur.roleId = :roleId and ur.userId = :userId")
    UserRole findUserRolesById(@Param("roleId") Integer roleId,@Param("userId") Long userId);
	
}