package com.kafka.kafkaconsumer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleId.class)
public class UserRole {
	
	@Id
	@Column(name = "role_id")
	@JsonInclude(value = Include.CUSTOM)
	private Integer roleId;
	
	@Id
	@Column(name = "user_id")
	@JsonInclude(value = Include.CUSTOM)
	private Long userId;

	

	public UserRole() {
		// TODO Auto-generated constructor stub
	}

	public UserRole(Long userId, Integer roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "UserRole [userId=" + userId + ", roleId=" + roleId + "]";
	}

}