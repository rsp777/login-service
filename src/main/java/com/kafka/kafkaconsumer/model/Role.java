package com.kafka.kafkaconsumer.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pawar.todo.dto.PermissionDto;
import com.pawar.todo.dto.RoleDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer id;

	@Column(nullable = false, unique = true)
	private String name;

	@JsonInclude(value = Include.CUSTOM)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_permissions", // This should be your association table
			joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions = new HashSet<>();

//
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "roles_permissions", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns = {
//			@JoinColumn(name = "permission_id")
//
//	})
//	private Set<Permission> permissions = new HashSet<>();

	@JsonInclude(value = Include.CUSTOM)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	@JsonProperty("created_dttm")
	@Column(name = "created_dttm")
	private LocalDateTime createdDttm;

	@JsonInclude(value = Include.CUSTOM)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	@JsonProperty("last_updated_dttm")
	@Column(name = "last_updated_dttm")
	private LocalDateTime lastUpdatedDttm;

	@JsonInclude(value = Include.CUSTOM)
	@Column(name = "created_source")
	private String createdSource;

	@JsonInclude(value = Include.CUSTOM)
	@Column(name = "last_updated_source")
	private String lastUpdatedSource;

	public Role() {
	}

	public Role(Integer id, String name, Set<Permission> permissions, LocalDateTime createdDttm,
			LocalDateTime lastUpdatedDttm, String createdSource, String lastUpdatedSource) {
		super();
		this.id = id;
		this.name = name;
		this.permissions = permissions;
		this.createdDttm = createdDttm;
		this.lastUpdatedDttm = lastUpdatedDttm;
		this.createdSource = createdSource;
		this.lastUpdatedSource = lastUpdatedSource;
	}

	public Role(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Role(Integer id, String name, Set<Permission> permissions) {
		super();
		this.id = id;
		this.name = name;
		this.permissions = permissions;
	}

	public Role(RoleDto roleDto) {
		this.id = roleDto.getRole_id();
		this.name = roleDto.getName();
		this.permissions = convertPermissionDtoToEntity(roleDto.getPermissions());
		this.createdDttm = roleDto.getCreatedDttm();
		this.lastUpdatedDttm = roleDto.getLastUpdatedDttm();
		this.createdSource = roleDto.getCreatedSource();
		this.lastUpdatedSource = roleDto.getLastUpdatedSource();
	}

	public Integer getRole_id() {
		return id;
	}

	public void setRole_id(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public LocalDateTime getCreatedDttm() {
		return createdDttm;
	}

	public void setCreatedDttm(LocalDateTime createdDttm) {
		this.createdDttm = createdDttm;
	}

	public LocalDateTime getLastUpdatedDttm() {
		return lastUpdatedDttm;
	}

	public void setLastUpdatedDttm(LocalDateTime lastUpdatedDttm) {
		this.lastUpdatedDttm = lastUpdatedDttm;
	}

	public String getCreatedSource() {
		return createdSource;
	}

	public void setCreatedSource(String createdSource) {
		this.createdSource = createdSource;
	}

	public String getLastUpdatedSource() {
		return lastUpdatedSource;
	}

	public void setLastUpdatedSource(String lastUpdatedSource) {
		this.lastUpdatedSource = lastUpdatedSource;
	}

	public Set<Permission> convertPermissionDtoToEntity(Set<PermissionDto> permissionDtos) {

		Set<Permission> permissions = new HashSet<>();

		for (PermissionDto permissionDto : permissionDtos) {

			Permission permission = new Permission(permissionDto);
			permissions.add(permission);
		}
		return permissions;

	}
	
	public Set<PermissionDto> convertPermissionEntityToDto(Set<Permission> permissions) {

		Set<PermissionDto> permissionDtos = new HashSet<>();
		PermissionDto permissionDto = new PermissionDto();

		for (Permission permission : permissions) {
			permissionDto.setId(permission.getId());
			permissionDto.setName(permission.getName());
			permissionDto.setCreatedDttm(permission.getCreatedDttm());
			permissionDto.setLastUpdatedDttm(permission.getLastUpdatedDttm());
			permissionDto.setCreatedSource(permission.getCreatedSource());
			permissionDto.setLastUpdatedSource(permission.getLastUpdatedSource());
			permissionDtos.add(permissionDto);
		}
		return permissionDtos;

	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", permissions=" + permissions + ", createdDttm="
				+ createdDttm + ", lastUpdatedDttm=" + lastUpdatedDttm + ", createdSource=" + createdSource
				+ ", lastUpdatedSource=" + lastUpdatedSource + "]";
	}

}
