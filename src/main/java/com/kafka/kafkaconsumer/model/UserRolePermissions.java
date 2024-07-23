package com.kafka.kafkaconsumer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_roles_permissions")
@IdClass(UserRolePermissionsId.class)
public class UserRolePermissions {
	
	@Id
	@Column(name = "role_id")
	private Integer roleId;
	
	@Id
	@Column(name = "user_id")
	private Long userId;

	@Id
	@Column(name = "permission_id")
	private Integer permissionId;

	public UserRolePermissions() {
		// TODO Auto-generated constructor stub
	}

	public UserRolePermissions(Long userId, Integer roleId,Integer permissionId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
		this.permissionId = permissionId;
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
	
	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public String toString() {
		return "UserRolePermissions [roleId=" + roleId + ", userId=" + userId + ", permissionId=" + permissionId + "]";
	}
}
