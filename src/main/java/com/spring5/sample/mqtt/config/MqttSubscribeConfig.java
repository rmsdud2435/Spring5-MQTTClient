package com.spring5.sample.mqtt.config;

public class MqttSubscribeConfig {

	private String 	broker;
	private String 	clientId;
	private String 	userName;
	private String 	userPwd;
	private String 	topic;
	private int 	qos;
	private int 	sessionTimeout;
	private String 	protocol;
	private int 	port;
	
	public MqttSubscribeConfig(){}
	
	public MqttSubscribeConfig(String broker, String clientId, String userName, String userPwd, String topic, int qos, int sessionTimeout, String protocol, int port) {
		super();
		this.broker = broker;
		this.clientId = clientId;
		this.userName = userName;
		this.userPwd = userPwd;
		this.topic = topic;
		this.qos = qos;
		this.sessionTimeout = sessionTimeout;
		this.protocol = protocol;
		this.port = port;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	@Override
	public String toString() {
		return "MqttConfig [broker=" + broker + ", clientId=" + clientId + ", userName=" + userName + ", userPwd="
				+ userPwd + ", topic=" + topic + ", qos=" + qos + ", sessionTimeout=" + sessionTimeout + ", protocol="
				+ protocol + ", port=" + port + "]";
	}
}
