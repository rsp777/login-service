package com.kafka.kafkaconsumer.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kafka.kafkaconsumer.exception.TokenNotFoundException;
import com.kafka.kafkaconsumer.model.RefreshTokens;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {

	@Query("SELECT rt FROM RefreshTokens rt WHERE rt.user.id = :id AND rt.revoked = false")
	Optional<RefreshTokens> findByUserId(Long id) throws TokenNotFoundException;
	
	@Query("SELECT rt FROM RefreshTokens rt WHERE rt.token = :token")
	Optional<RefreshTokens> findBytoken(String token);
	
//	@Modifying
//    @NamedNativeQuery(value = "update RefreshTokens set revoked = :revoked where id = :id")
//	Optional<?> revoked(@Param("revoked") Boolean revoked,@Param("id") Long id);
	
}
