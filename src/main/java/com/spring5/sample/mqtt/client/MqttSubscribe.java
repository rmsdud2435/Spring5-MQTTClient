package com.spring5.sample.mqtt.client;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring5.sample.mqtt.config.MqttSubscribeConfig;
import com.spring5.sample.mqtt.service.IMqttSubscribeService;
import com.spring5.sample.mqtt.util.SSLUtil;

@Service
public class MqttSubscribe implements MqttCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private  	String 					broker;
	private  	String 					clientId;
	private  	String 					userName;
	private  	String 					userPwd;
	private  	String 					topic;
	private		int						qos = 0;
	private		String					protocol;
	
	private MemoryPersistence 			persistence;
	private MqttConnectOptions 			connectOptions;
	private MqttClient 					client;
	
	public MqttSubscribe(MqttSubscribeConfig mqttConfig){
		this.broker 		= mqttConfig.getBroker() + ":" + mqttConfig.getPort();
		
		//서비스를 redeploy할 때 제대로 꺼지지 않아 clientId가 중복되어 publish가 안된 적이 있어서 아래와 같이 수정
		this.clientId 		= mqttConfig.getClientId() + "/" + UUID.randomUUID();
		this.userName 		= mqttConfig.getUserName();
		this.userPwd 		= mqttConfig.getUserPwd();
		this.topic 			= mqttConfig.getTopic();
		this.qos 			= mqttConfig.getQos();
		this.protocol		= mqttConfig.getProtocol();
		logger.info("bizMOB-subscriber : {}", mqttConfig.toString() );
	}
	
	public void init(){
		try {
			connectOptions = new MqttConnectOptions();
			if(userName != null && !"".equals(userName)){
				connectOptions.setUserName(this.userName);
				connectOptions.setPassword(this.userPwd.toCharArray());
			}
			
			if("SSL".equalsIgnoreCase(protocol)) {		
				SSLSocketFactory ssl = SSLUtil.getSSLSocketFactory();
				connectOptions.setSocketFactory(ssl);
				this.broker = "ssl://" + broker;
			} else if("WSS".equalsIgnoreCase(protocol)){
				SSLSocketFactory ssl = SSLUtil.getSSLSocketFactory();
				connectOptions.setSocketFactory(ssl);
				this.broker = "wss://" + broker;
			} else if("WS".equalsIgnoreCase(protocol)){
				this.broker = "ws://" + broker;
			}

			connectOptions.setCleanSession(true);
			this.persistence = new MemoryPersistence();
			client = new MqttClient(this.broker, this.clientId, this.persistence);
			client.setCallback(this);
			
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
				 client.unsubscribe(topic);
				 client.disconnect();
			 }
			 
			 client.close();
			 
		 } catch (MqttException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	public void subscribe(){
		
		int retryCount = 0;
		while(true) {
			try {
				
				if(retryCount > 10) {
					logger.warn("Not Connected MQTT Server. topic : {}", topic);
					break;
				} else {
					if(!client.isConnected()) {
						logger.info("Try Connecting to broker : {}, connecting count : {}", this.broker, retryCount);
						retryCount++;
						
						client.connect(connectOptions);
						
						Thread.sleep(500);
					} else {
						client.subscribe(topic, qos);
						logger.info("connected topic :: {}", topic);
						break;
					}
				}
			} catch (MqttException e) {
				logger.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

		//mqttCilentService.process(topic, mqttMessage);

    }
	
	@Override
	public void connectionLost(Throwable cause) {
		 logger.warn("Lost Connection." + cause.getCause());	 
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		logger.info("Message with " + iMqttDeliveryToken + " delivered.");
	}
	
	public boolean isLive() {
		boolean result = true;
		
		if(!client.isConnected()) {
			result = false;
			logger.info("MQTT server not Connected ! ::  Topic : {}", topic);
		} else {
			logger.trace("MQTT server Connected ! ::  Topic : {}", topic);
		}
		
		return result;
	}
}
