package com.kafka.kafkaconsumer.utils;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafkaconsumer.model.User;

public class MessageDeserializer implements Deserializer<User> {
	private static final Logger logger = LoggerFactory.getLogger(MessageDeserializer.class);

    @Override
    public User deserialize(String topic, byte[] data) {

    	User message = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(data, User.class);
            logger.info("message : {} ",message);

        } catch (IOException e) {

        }

        return message;
    }
}
