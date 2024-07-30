package com.kafka.kafkaconsumer.model;

import java.io.Serializable;
import java.util.Objects;

public class UserRolePermissionsId implements Serializable {
	private Integer roleId;
	private Long userId;
	private Integer permissionId;

	public UserRolePermissionsId() {
	}

	public UserRolePermissionsId(Integer roleId,Long userId,Integer permissionId) {

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
	public int hashCode() {
		return Objects.hash(roleId, userId, permissionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRolePermissionsId other = (UserRolePermissionsId) obj;
		return Objects.equals(roleId, other.roleId) && Objects.equals(userId, other.userId)&& Objects.equals(permissionId, other.permissionId);
	}

	@Override
	public String toString() {
		return "UserRolePermissionsId [roleId=" + roleId + ", userId=" + userId + ", permissionId=" + permissionId
				+ "]";
	}
}
