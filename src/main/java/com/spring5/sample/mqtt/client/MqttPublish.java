package com.spring5.sample.mqtt.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring5.sample.mqtt.config.MqttPublishConfig;
import com.spring5.sample.mqtt.util.SSLUtil;


public class MqttPublish implements MqttCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	private String 				broker;
	private String 				clientId;
	private String 				userName;
	private	String 				userPwd;
	private	String				protocol;
	
	private MemoryPersistence 	persistence;
	private MqttConnectOptions 	connectOptions;
	private MqttAsyncClient 	client;
	
	public MqttPublish(MqttPublishConfig mqttConfig){
		this.broker 		= mqttConfig.getBroker() + ":" + mqttConfig.getPort();
		this.clientId 		= mqttConfig.getClientId();
		this.userName 		= mqttConfig.getUserName();
		this.userPwd 		= mqttConfig.getUserPwd();
		this.protocol		= mqttConfig.getProtocol();
		logger.info("bizMOB-publish : {}", mqttConfig.toString() );
	}
	
	public void init(){
		
		try {
			connectOptions = new MqttConnectOptions();
			if(userName != null && !"".equals(userName)){
				connectOptions.setUserName(this.userName);
				connectOptions.setPassword(this.userPwd.toCharArray());
				/*
					동시에 트래픽 핸들할 수 있는  갯수를 의미 -> default가 20정도로 매우 낮음.
					실제로 운영 중에 핸들할 수 있는 20개보다 많아서 에러난 경우가 많았다. 
				*/
				connectOptions.setMaxInflight(9999999);
				
			}
			
			if("SSL".equalsIgnoreCase(protocol)) {
				SSLSocketFactory ssl = SSLUtil.getSSLSocketFactory();
				connectOptions.setSocketFactory(ssl);
				if(broker.indexOf("ssl://") == -1 ){					
					broker = "ssl://" + broker;
				}
			} else if("WSS".equalsIgnoreCase(protocol)){
				SSLSocketFactory ssl = SSLUtil.getSSLSocketFactory();
				connectOptions.setSocketFactory(ssl);
				if(broker.indexOf("wss://") == -1 ){					
					broker = "wss://" + broker;
				}
			} else if("WS".equalsIgnoreCase(protocol)){
				if(broker.indexOf("ws://") == -1 ){					
					broker = "ws://" + broker;
				}
			} else{
				if(broker.indexOf(protocol+"://") == -1 ){					
					broker = protocol + "://" + broker;
				}
			}

			connectOptions.setCleanSession(true);
			this.persistence = new MemoryPersistence();
			client = new MqttAsyncClient(this.broker, this.clientId, this.persistence);
			client.setCallback(this);
			client.connect(connectOptions);
			Thread.sleep(2000);
			
		} catch(MqttException me) {
			logger.error("reason : {}", me.getReasonCode());
			logger.error("msg : {}", me.getMessage());
			logger.error("loc : {}", me.getLocalizedMessage());
			logger.error("cause : {}", me.getCause());
			logger.error("excep : {}", me);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 	
	}
	
	public void disconnect(){
		 try {
			 if(client.isConnected()) {				
				 client.disconnect();
			 }
			 
			 client.close();
			 
		 } catch (MqttException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public boolean publish(String topic, String msg, int qos) throws InterruptedException{
		
		MqttMessage message = new MqttMessage();
		
		try {
			message.setQos(qos);
			message.setPayload(msg.getBytes());
			
			if(!client.isConnected()) {
				init();
			}
			
			client.publish(topic, message);
			return true;
			
		} catch (MqttPersistenceException e) {
			logger.error(e.getMessage(), e);
		} catch (MqttException e) {
			logger.error(e.getMessage(), e);
		} 
		
		return false;
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		
    	String messagePayload = new String(mqttMessage.getPayload(), "UTF-8");
    	logger.debug("Message arrived : {}", messagePayload);
    	
//    	ReceivedStatusMessage convertMessage = JsonUtil.toObject(messagePayload, ReceivedStatusMessage.class);
    }
	
	@Override
	public void connectionLost(Throwable cause) {
		logger.warn("Lost Connection." + cause.getCause());

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		logger.info("Message with " + iMqttDeliveryToken + " delivered.");
	}
}
