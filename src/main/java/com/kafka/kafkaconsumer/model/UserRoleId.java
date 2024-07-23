package com.kafka.kafkaconsumer.model;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleId implements Serializable {
	private Integer roleId;
	private Long userId;

	public UserRoleId() {
		// TODO Auto-generated constructor stub
	}

	public UserRoleId(Integer roleId, Long userId) {

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
	public int hashCode() {
		return Objects.hash(roleId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRoleId other = (UserRoleId) obj;
		return Objects.equals(roleId, other.roleId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "UserRoleId [roleId=" + roleId + ", userId=" + userId + "]";
	}

}