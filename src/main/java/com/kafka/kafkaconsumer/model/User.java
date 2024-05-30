package com.kafka.kafkaconsumer.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

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

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "logged_in", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean loggedIn = false;
	
	@Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isActive = false;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "roles",
//        joinColumns = @JoinColumn(name = "user_id"),
//        inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", // This should be your association table
			joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {

	}

	public User(Long user_id, String username, String email, String passwordHash, Date createdAt, Date updatedAt,
			Boolean loggedIn, Set<Role> roles) {
		this.id = user_id;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.loggedIn = loggedIn;
		this.roles = roles;
	}

	public User(UserDto userDto) {
		this.id = userDto.getUserId();
		this.username = userDto.getUsername();
		this.email = userDto.getEmail();
		this.passwordHash = userDto.getpasswordHash();
		this.createdAt = userDto.getCreatedAt();
		this.updatedAt = userDto.getUpdatedAt();
		this.loggedIn = userDto.getLoggedIn();
		this.roles = convertDtoToEntity(userDto.getRoles());		
	}

	public Long getUser_id() {
		return id;
	}

	public void setUser_id(Long user_id) {
		this.id = user_id;
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

	@Override
	public String toString() {
		return "User [user_id=" + id + ", username=" + username + ", email=" + email + ", passwordHash="
				+ passwordHash + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", loggedIn=" + loggedIn
				+ ", roles=" + roles + "]";
	}
	
	public Set<Role> convertDtoToEntity(Set<RoleDto> rolesDto){
		
		System.out.println("rolesdto : {}"+rolesDto);
		Set<Role> roles = new HashSet<>();
	    for (RoleDto roleDto : rolesDto) {
	        Role role = new Role();
	        role.setRole_id(roleDto.getRole_id());
	        role.setName(roleDto.getName());
	        roles.add(role);
	    }
	    return roles;
	}
	
	public Set<RoleDto> convertEntityToDto(Set<Role> roles) {

		Set<RoleDto> roleDtos = new HashSet<>();

		for (Role role : roles) {
			RoleDto roleDto = new RoleDto(role.getName());
			roleDtos.add(roleDto);
		}
		return roleDtos;
	}
}
