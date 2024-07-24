package com.kafka.kafkaconsumer.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pawar.todo.dto.RoleDto;
import com.pawar.todo.dto.UserDto;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@DynamicUpdate
@SqlResultSetMapping(
	    name = "updateResultt",
	    columns = { @ColumnResult(name = "count") }
	)
	@NamedNativeQueries({
	    @NamedNativeQuery(
	        name = "updateLoggedIn",
	        query = "UPDATE users SET logged_in = :logged_in WHERE user_id = :user_id",
	        resultSetMapping = "updateResultt"
	    )
	})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@JsonInclude(value = Include.CUSTOM)
	@Column(name = "created_at")
	private Date createdAt;

	@JsonInclude(value = Include.CUSTOM)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "logged_in", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean loggedIn = false;

	@JsonInclude(value = Include.CUSTOM)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", // This should be your association table
			joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {

	}

	public User(Long id, String username, String email, String passwordHash, String firstName, String middleName,
			String lastName, Date createdAt, Date updatedAt, Boolean loggedIn, Set<Role> roles) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.loggedIn = loggedIn;
		this.roles = roles;
	}

	public User(String username, String email, String passwordHash, String firstName, String middleName,
			String lastName) {
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}

	public User(UserDto userDto) {
		this.id = userDto.getUserId();
		this.username = userDto.getUsername();
		this.email = userDto.getEmail();
		this.passwordHash = userDto.getpasswordHash();
		this.firstName = userDto.getFirstName();
		this.middleName = userDto.getMiddleName();
		this.lastName = userDto.getLastName();
		this.createdAt = userDto.getCreatedAt();
		this.updatedAt = userDto.getUpdatedAt();
		this.loggedIn = userDto.getLoggedIn();
		this.roles = convertRolesDtoToEntity(userDto.getRoles());
	}

	public Long getUser_id() {
		return id;
	}

	public void setUser_id(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", passwordHash="
				+ passwordHash + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", loggedIn=" + loggedIn + ", userRoles="
				+ roles + "]";
	}

	public Set<RoleDto> convertRolesEntityToDto(Set<Role> roles) {

		Set<RoleDto> roleDtos = new HashSet<>();
		RoleDto roleDto = new RoleDto();
		for (Role role : roles) {
			roleDto.setRole_id(role.getRole_id());
			roleDto.setName(role.getName());
			roleDto.setPermissions(role.convertPermissionEntityToDto(role.getPermissions()));
			roleDto.setCreatedDttm(role.getCreatedDttm());
			roleDto.setLastUpdatedDttm(role.getLastUpdatedDttm());
			roleDto.setCreatedSource(role.getCreatedSource());
			roleDto.setLastUpdatedSource(role.getLastUpdatedSource());
			roleDtos.add(roleDto);
		}
		return roleDtos;

	}

	public Set<Role> convertRolesDtoToEntity(Set<RoleDto> roleDtos) {

		Set<Role> roles = new HashSet<>();

		for (RoleDto roleDto : roleDtos) {
			Role role = new Role(roleDto);
			roles.add(role);
		}
		return roles;

	}

}
