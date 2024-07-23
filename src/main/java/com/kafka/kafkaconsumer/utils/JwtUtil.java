package com.kafka.kafkaconsumer.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.exception.TokenNotFoundException;
import com.kafka.kafkaconsumer.exception.UserNotFoundException;
import com.kafka.kafkaconsumer.model.RefreshTokens;
import com.kafka.kafkaconsumer.model.User;
import com.kafka.kafkaconsumer.repository.RefreshTokensRepository;
import com.kafka.kafkaconsumer.repository.UserRepository;
import com.kafka.kafkaconsumer.service.UserService;
import com.pawar.todo.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

@Component
@EntityListeners(RefreshTokens.class)
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Autowired
	private RefreshTokensRepository refreshTokensRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	private String secretKey = "secretKey";
	private Long expirationTime = (long) 50000.0000;

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	public JwtUtil() {
		httpClient = HttpClients.createDefault();
		objectMapper = new ObjectMapper();
	}

	public String generateToken(UserDto userDto) throws ClientProtocolException, IOException, TokenNotFoundException {

		Map<String, Object> claims = new HashMap<>();
		Boolean revoked = false;
		logger.info("userDto name {} ", userDto.getUsername());
		RefreshTokens refreshTokens = new RefreshTokens();
		String token = "";
		User loggerInUser = userRepository.findByUsername(userDto.getUsername()).get();
		logger.info("Logged In User : {}", loggerInUser);
		Boolean isTokenAlreadyAvailable = isTokenAlreadyAvailable(loggerInUser);
		logger.info("isTokenAlreadyAvailable : {}", isTokenAlreadyAvailable);

		if (!isTokenAlreadyAvailable) {
			token = createToken(claims, userDto.getFirstName() + " " + userDto.getMiddleName() + " "
					+ userDto.getLastName() + " " + userDto.getUsername());

			Date dateOfExpiration = extractClaim(token, claimss -> claimss.getExpiration());
			Date dateofIssue = extractClaim(token, claimss -> claimss.getIssuedAt());
			String username = extractClaim(token, claimss -> claimss.getSubject());
			refreshTokens.setToken(token);
			refreshTokens.setCreatedAt(dateofIssue);
			refreshTokens.setExpires(dateOfExpiration);
			refreshTokens.setUser(loggerInUser);
			refreshTokens.setRevoked(false);
			logger.info("Refresh Token : {}", refreshTokens);

			logger.info("dateOfExpiration: {}", dateOfExpiration);
			refreshTokensRepository.save(refreshTokens);
			loggerInUser.setLoggedIn(true);
			userRepository.save(loggerInUser);

		} else {

			refreshTokens = refreshTokensRepository.findByUserId(loggerInUser.getUser_id()).get();
			boolean isTokenExpired = isTokenExpired(JWT.decode(refreshTokens.getToken()));
			logger.info("isTokenExpired : {}", isTokenExpired);
			logger.info(token);
			if (isTokenExpired) {
				int count = handleTokenExpiration(refreshTokens);
				if (count > 0) {
					token = createToken(claims, userDto.getFirstName() + " " + userDto.getMiddleName() + " "
							+ userDto.getLastName() + " " + userDto.getUsername());
					Date dateOfExpiration = extractClaim(token, claimss -> claimss.getExpiration());
					Date dateofIssue = extractClaim(token, claimss -> claimss.getIssuedAt());
					String username = extractClaim(token, claimss -> claimss.getSubject());
					refreshTokens.setToken(token);
					refreshTokens.setCreatedAt(dateofIssue);
					refreshTokens.setExpires(dateOfExpiration);
					refreshTokens.setUser(loggerInUser);
					refreshTokens.setRevoked(false);
					logger.info("Refresh Token : {}", refreshTokens);

					logger.info("dateOfExpiration: {}", dateOfExpiration);
					refreshTokensRepository.save(refreshTokens);
					loggerInUser.setLoggedIn(true);
					userRepository.save(loggerInUser);
					token = refreshTokens.getToken();
					logger.info("Token : {}", token);
					return token;
				}
			}
			token = refreshTokens.getToken();
			return token;
		}

		logger.info("User is logged in : {}", loggerInUser);
		logger.info("logged in user : {}", loggerInUser.getLoggedIn());
		return token;
	}

	private String createToken(Map<String, Object> claims, String subject) {
		logger.info("Subject : {}", subject);
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	public int handleTokenExpiration(RefreshTokens token) {

		token.setRevoked(true);
		logger.info(token.getRevoked() + " " + token.getId());

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Boolean newRevokedStatus = token.getRevoked();
		Long tokenId = token.getId();
		Query updateQuery = entityManager.createNamedQuery("updateRevokedStatus")
				.setParameter("revoked", newRevokedStatus).setParameter("id", tokenId);
		int updatedCount = updateQuery.executeUpdate();
		logger.info("Update query executed, Affected Rows : {}", updatedCount);

		entityManager.getTransaction().commit();
		entityManager.close();
		return updatedCount;
//		}
	}

	public Boolean isTokenAlreadyAvailable(User user) throws TokenNotFoundException {

		logger.info("User : {}", user);
		Optional<RefreshTokens> refreshTokens = refreshTokensRepository.findByUserId(user.getUser_id());

		logger.info("refreshTokens.isPresent : {}", refreshTokens.isPresent());

		if (refreshTokens.isPresent()) {

			if (refreshTokens.get().getUser().getUser_id() == user.getUser_id()) {
				logger.info("user is equal");
				return true;
			} else {
				logger.info("user is not equal");

				return false;
			}
		}
		return false;

	}

	public Boolean validateToken(String token, UserDto userDto) {
		final String username = extractUsername(token);
		return (username.equals(userDto.getUsername()) && !isTokenExpired(JWT.decode(token)));
	}

	public String extractUsername(String token) {

		String[] decodedString = extractClaim(token, Claims::getSubject).split(" ");
		String user_name = "";
		user_name = decodedString[decodedString.length - 1];

		return user_name;
	}

	public Date extractExpiration(String token) {
		return (Date) extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(DecodedJWT decodedJWT) throws ExpiredJwtException {
		Date expiresAt = decodedJWT.getExpiresAt();
		return expiresAt.before(new Date());
	}

	public void invalidateToken(String token) throws UserNotFoundException {

		logger.info("token : {}", token);
		Optional<RefreshTokens> refreshTokens = refreshTokensRepository.findBytoken(token);
		logger.info(refreshTokens.toString());
		refreshTokensRepository.delete(refreshTokens.get());
		logger.info("Token is deleted");

	}

	public void signOut(String token) throws UserNotFoundException {
		String username = extractUsername(token);
		UserDto userDto = userService.getUserByName(username);
		User user = userService.convertDtoToEntity(userDto);
		user.setLoggedIn(false);
		logger.info(user.toString());
		loggedOut(user);
	}

	public int loggedOut(User user) {
		logger.info("Logged Out User : {}", user);

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Boolean newLoggedIn = user.getLoggedIn();
		logger.info("newLoggedIn : {}", newLoggedIn);
		Long userId = user.getUser_id();
		logger.info("userId : {}", userId);

		Query updateQuery = entityManager.createNamedQuery("updateLoggedIn").setParameter("logged_in", newLoggedIn)
				.setParameter("user_id", userId);
		int updatedCount = updateQuery.executeUpdate();
		logger.info("Update query executed, Affected Rows : {}", updatedCount);

		entityManager.getTransaction().commit();
		entityManager.close();
		return updatedCount;

	}
}
