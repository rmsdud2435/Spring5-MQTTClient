package com.spring5.sample.mqtt.config;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicDO {

	private String topic;
	private String delim;
	private String a;
	private String b;
	private String c;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public TopicDO(String topic, String delim) throws Exception {

		this.topic = topic;
		if (delim == null) {
			this.delim = "/";
		} else {
			this.delim = delim;
		}
		List<String> strToken = new ArrayList<String>();
		try {
			StringTokenizer tokenizer = new StringTokenizer(topic, delim);
			while (tokenizer.hasMoreTokens()) {
				strToken.add(tokenizer.nextToken());
			}
		} catch (Exception e) {
			logger.warn("Too Many Topic keyword : {}", topic);			
		}

		/*
		 * 
		 * 예를 들면 아래와 같이 객체화 시킬 수 있다.
		 * 
		 * this.clientName 	= strToken.get(0);
		 * this.callType 	= strToken.get(1);
		 * this.carNumber 	= strToken.get(2);
		 * 	
		 */
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDelim() {
		return delim;
	}

	public void setDelim(String delim) {
		this.delim = delim;
	}
	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return "TopicDO [topic=" + topic + ", a=" + a + ", b=" + b	+ ", c=" + c + "]";
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}


}
