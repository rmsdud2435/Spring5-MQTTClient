package com.spring5.sample.mqtt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageConverter {
	private static final Logger logger = LoggerFactory.getLogger(MessageConverter.class);
	
	private static ObjectMapper	mapper;
	
	public static <T> T convertMessage(String messagePayload, Class<T> convertMessage) throws Exception{
		if(mapper == null) {
			mapper 	= new ObjectMapper();
		}
		
    	T t = mapper.readValue(messagePayload, convertMessage);
    	
    	return t;
	}
}
