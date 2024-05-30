package com.kafka.kafkaconsumer.model;

import java.util.Date;

import com.kafka.kafkaconsumer.utils.JwtUtil;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "refresh_tokens")
@SqlResultSetMapping(
	    name = "updateResult",
	    columns = { @ColumnResult(name = "count") }
	)
	@NamedNativeQueries({
	    @NamedNativeQuery(
	        name = "updateRevokedStatus",
	        query = "UPDATE refresh_tokens SET revoked = :revoked WHERE id = :id",
	        resultSetMapping = "updateResult"
	    )
	})
@EntityListeners(JwtUtil.class)
public class RefreshTokens {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expires;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "revoked", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean revoked;

	
	public RefreshTokens() {}
	
	public RefreshTokens(Long id, User user, String token, Date expires, Date createdAt, Boolean revoked) {
		this.id = id;
		this.user = user;
		this.token = token;
		this.expires = expires;
		this.createdAt = createdAt;
		this.revoked = revoked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}

	@Override
	public String toString() {
		return "RefreshTokens [id=" + id + ", user=" + user + ", token=" + token + ", expires=" + expires
				+ ", createdAt=" + createdAt + ", revoked=" + revoked + "]";
	}	
}