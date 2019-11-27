package com.spring5.sample.mqtt.service.impl;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spring5.sample.mqtt.config.MessagesType;
import com.spring5.sample.mqtt.config.TopicDO;
import com.spring5.sample.mqtt.service.IMqttSubscribeService;
import com.spring5.sample.mqtt.util.MessageConverter;

public class MqttSubscribeServiceImpl implements IMqttSubscribeService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public <T> T convertMessage(MqttMessage mqttMessage, Class<T> convertMessage, String encoding) {
		
    	try {
    		String messagePayload = new String(mqttMessage.getPayload(), encoding);
        	T t = MessageConverter.convertMessage(messagePayload, convertMessage);
        	
        	return t;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public JsonNode process(String topic, MqttMessage mqttMessage, String encoding) throws Exception{
		
		ObjectNode resultNode = new ObjectMapper().createObjectNode();
		if(encoding == null) encoding = "UTF-8";
		
		try {
			
			//MQTT의 메세지는 일반적으로 text만 보낼 수 있어 convertMessage라는 클래스를 만들어 JsonNode로 만듬
			JsonNode messageNode 	= convertMessage(mqttMessage, JsonNode.class, encoding);
			
			//MQTT의 Topic 또한 일반적으로 text만 보낼 수 있어 /를 통해 객체화 시켜서 그 값들을 구분
			TopicDO  topicDO 		= new TopicDO(topic, "/");
			
			//callType에 따라 다른 액션을 주기위한 로직
			switch (MessagesType.valueOf(topic)) {
			
				case Hello:
					//Process when Topic is Hello
				case World:
					//Process when Topic is World
				default:
					logger.warn("Topic is Undefined ======> Received Topic :: {}, Message :: {}", topic, new String( mqttMessage.getPayload(), encoding) ); 
			}		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return resultNode;
	}

	@Override
	public JsonNode process(String topic, MqttMessage mqttMessage) throws Exception{
		return process(topic, mqttMessage, null);
	}
}


