package com.kafka.kafkaconsumer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId;

	@Column(nullable = false, unique = true)
	private String name;

	public Role() {

	}

	@JsonCreator
	public Role(@JsonProperty("role_id")Integer role_id,@JsonProperty("name") String name) {
		super();
		this.roleId = role_id;
		this.name = name;
	}

	public Integer getRole_id() {
		return roleId;
	}

	public void setRole_id(Integer role_id) {
		this.roleId = role_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Role [role_id=" + roleId + ", name=" + name + "]";
	}

}
