package com.kafka.kafkaconsumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.service.UserService;
import com.kafka.kafkaconsumer.utils.JwtUtil;
import com.pawar.todo.dto.JwtResponseDto;
import com.pawar.todo.dto.LoginDto;
import com.pawar.todo.dto.UserDto;

@RestController
@RequestMapping("/login-service")
public class LoginAuthController {

	private static final Logger logger = LoggerFactory.getLogger(LoginAuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/signin")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto loginDto) {

		try {

			// Authenticate the user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPasswordHash()));
			logger.info("Authentication Successful : {}", loginDto.getUsername());
			// If authentication was successful, proceed with generating the JWT token
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final UserDto userDto = userService.getUserByName(loginDto.getUsername());
			logger.info("userDto : {}", userDto);
			final String token = jwtUtil.generateToken(userDto);

			// Return the token in the response
			return ResponseEntity.ok(new JwtResponseDto(token));

		} catch (Exception e) {
			// If authentication fails, return an appropriate response
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
		}

	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/signout")
	public ResponseEntity<?> signOut(@RequestHeader(value = "Authorization") String token) {
		try {
			// Remove the "Bearer " prefix from the token
			if (token != null && token.startsWith("Bearer ")) {
				token = token.substring(7);
				logger.info("token : {}", token);
			}
			DecodedJWT decodedJWT = JWT.decode(token);
			String decodedSubject = decodedJWT.getSubject();
			logger.info("Decoded Subject : {}", decodedSubject);
	        ObjectMapper om = new ObjectMapper();
	        
			String[] decodedString = decodedJWT.getSubject().split("\\|");
			String user_name = "";
			for (int i = 2; i < decodedString.length; i++) {
				user_name = decodedString[i];
				logger.info("decodedString["+i+"] : {}",decodedString[i]);
			}
//			user_name = decodedString[2];
			logger.info("Subject : " + user_name);
			UserDto dto = userService.getUserByName(user_name);
			logger.info("UserDto : {}", dto.toString());

			if (dto.getLoggedIn().equals(true)) {
				logger.info("Is Logged in : {}", dto.getLoggedIn());
				jwtUtil.invalidateToken(token);
				jwtUtil.signOut(token);
				logger.info("User has been signed out successfully.");

			} else {
				logger.info("user is already signed out.");
			}

			// Return a successful sign-out message
			return ResponseEntity.status(HttpStatus.OK).body("Logged out Successfully");

		} catch (Exception e) {
			// If sign-out fails, return an appropriate response
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed: " + e.getMessage());
		}
	}

}
