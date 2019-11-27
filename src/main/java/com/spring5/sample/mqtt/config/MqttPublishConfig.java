package com.spring5.sample.mqtt.config;

public class MqttPublishConfig {

	private String broker;
	private String clientId;
	private String userName;
	private String userPwd;
	private int sessionTimeout;
	private String protocol;
	private int port;
	
	public MqttPublishConfig(){}
	
	public MqttPublishConfig(String broker, String clientId, String userName, String userPwd, int sessionTimeout, String protocol, int port) {
		super();
		this.broker = broker;
		this.clientId = clientId;
		this.userName = userName;
		this.userPwd = userPwd;
		this.sessionTimeout = sessionTimeout;
		this.protocol = protocol;
		this.port = port;
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
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
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
}
