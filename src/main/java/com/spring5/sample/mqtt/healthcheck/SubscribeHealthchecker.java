package com.spring5.sample.mqtt.healthcheck;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring5.sample.mqtt.client.MqttSubscribe;

public class SubscribeHealthchecker {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	private MqttSubscribe mqttSubscribe;
	
	//Define the method to be called as configured
    public void execute()
    {
    	logger.debug("Executed health check task on :: " + new Date());
    	
    	if( !mqttSubscribe.isLive() ) {
    		mqttSubscribe.subscribe();
    	} else {
    		
    	}
    }
}
