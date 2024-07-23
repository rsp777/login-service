package com.kafka.kafkaconsumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.exception.UserNotFoundException;
import com.kafka.kafkaconsumer.model.Permission;
import com.kafka.kafkaconsumer.model.Role;
import com.kafka.kafkaconsumer.model.User;
import com.kafka.kafkaconsumer.repository.PermissionRepository;
import com.kafka.kafkaconsumer.repository.RoleRepository;
import com.kafka.kafkaconsumer.repository.UserRepository;
import com.pawar.todo.dto.RoleDto;
import com.pawar.todo.dto.UserDto;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;

	private static final String NEW_USER_TOPIC = "TO.DO.NEW.USER";
	private static final String NEW_ROLE_TOPIC = "TO.DO.NEW.ROLE";
	private static final String NEW_PERMISSION_TOPIC = "TO.DO.NEW.PERMISSION";
	

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@KafkaListener(topics = NEW_USER_TOPIC)
	public void userListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {

		try {
			String key = consumerRecord.key();
			String value = consumerRecord.value();
			int partition = consumerRecord.partition();
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(value, User.class);

			logger.info("value : {}", value);
			logger.info("Consumed message : " + user + " with key : " + key + " from partition : " + partition);
			if (value != null) {
				userRepository.save(user);
				logger.info("User saved to Login database : {}", user);

				ack.acknowledge();
			} else {
				logger.warn("Received null value from Kafka topic. {}");
			}
		} catch (Exception e) {
			logger.error("Error processing Kafka message: {}", e.getMessage());
			// Handle the exception (e.g., log, retry, or skip)
		}
	}

	@KafkaListener(topics = NEW_ROLE_TOPIC)
	public void userRoleListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
		try {

			String key = consumerRecord.key();
			String value = consumerRecord.value();
			int partition = consumerRecord.partition();
			ObjectMapper mapper = new ObjectMapper();

			Role role = mapper.readValue(value, Role.class);
			logger.info("Consumed message : " + value + " with key : " + key + " from partition : " + partition);
			logger.info("role : {}", role);
			if (value != null) {
				roleRepository.save(role);
				logger.info("Role saved to Login database: {}", value);

				ack.acknowledge();
			}

			ack.acknowledge(); // Acknowledge successful processing
		} catch (Exception e) {
			logger.error("Error processing Kafka message: {}", e.getMessage());
		}

	}
	
	@KafkaListener(topics = NEW_PERMISSION_TOPIC)
	public void permissionListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {

		try {
			String key = consumerRecord.key();
			String value = consumerRecord.value();
			int partition = consumerRecord.partition();
			ObjectMapper mapper = new ObjectMapper();
			Permission permission = mapper.readValue(value, Permission.class);

			logger.info("value : {}", value);
			logger.info("Consumed message : " + permission + " with key : " + key + " from partition : " + partition);
			if (value != null) {
				permissionRepository.save(permission);
				logger.info("Permission saved to Login database : {}", permission);

				ack.acknowledge();
			} else {
				logger.warn("Received null value from Kafka topic. {}");
			}
		} catch (Exception e) {
			logger.error("Error processing Kafka message: {}", e.getMessage());
			// Handle the exception (e.g., log, retry, or skip)
		}
	}

	private Integer getRoleIdFromRoleDtos(Set<RoleDto> roleDtos) {

		logger.info("RoleDtos : {}", roleDtos.toString());

		for (RoleDto roleDto : roleDtos) {
			Integer roleId = roleDto.getRole_id();
			logger.info("roleId : {}", roleId);

			if (roleId != null) {
				logger.info("roleId is not null : {}", roleId);
				return roleId;
			}
		}
		return null;
	}

	@Transactional
	public List<User> getAllUsers() throws UserNotFoundException {
		return userRepository.findAll();
	}

	private Role findRoleByName(RoleDto roleDto) {
		return roleRepository.findByName(roleDto.getName()).orElseThrow(() -> new RuntimeException("Role not found"));
	}

	private boolean emailExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	@Transactional
	public User getUserById(Long userId) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
	}

	@Transactional
	public UserDto getUserByName(String userName) throws UserNotFoundException {
		// TODO Auto-generated method stub
		logger.info("username : "+userName);
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found with username : {} " + userName));
		UserDto userDto = new UserDto(user.getUser_id(), user.getUsername(), user.getEmail(), user.getPasswordHash(),
				user.getFirstName(),user.getMiddleName(),user.getLastName(),user.getLoggedIn(), user.convertRolesEntityToDto(user.getRoles()));
		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameOrEmail(username, username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
				authorities);

	}

	public User convertDtoToEntity(UserDto userDto) {
		logger.info("User Dto : {}", userDto);
		User user = new User(userDto);
		return user;
	}

	public UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto(user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getLoggedIn());
		return userDto;
	}

	public List<UserDto> convertEntityToDto(List<User> users) {

		List<UserDto> userDtos = new ArrayList<>();

		for (User user : users) {
			UserDto userDto = new UserDto(user.getUsername(), user.getEmail(), user.getPasswordHash(),
					user.getLoggedIn());
			userDtos.add(userDto);
		}
		return userDtos;
	}

}
