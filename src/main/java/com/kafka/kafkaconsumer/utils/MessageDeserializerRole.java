package com.kafka.kafkaconsumer.utils;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.model.Role;

public class MessageDeserializerRole implements Deserializer<Role> {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageDeserializerRole.class);

	
	@Override
	public Role deserialize(String topic, byte[] data) {

		Role message = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			message = mapper.readValue(data, Role.class);
			logger.info("message : {}",message);
		} catch (IOException e) {

		}

		return message;

	}

}
