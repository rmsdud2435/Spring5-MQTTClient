package com.spring5.sample.mqtt.service;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.JsonNode;

public interface IMqttSubscribeService {
	
	public JsonNode process( String topic, MqttMessage mqttMessage, String encoding) throws Exception;
	
	public JsonNode process( String topic, MqttMessage mqttMessage) throws Exception;
	
}
