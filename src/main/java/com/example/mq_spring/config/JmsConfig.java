package com.example.mq_spring.config;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public MQConnectionFactory mqConnectionFactory() throws JMSException {
        MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
        mqConnectionFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        mqConnectionFactory.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
        mqConnectionFactory.setHostName("localhost");
        mqConnectionFactory.setPort(1414);
        mqConnectionFactory.setQueueManager("QM1");
        mqConnectionFactory.setChannel("DEV.APP.SVRCONN");
        mqConnectionFactory.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12");
        mqConnectionFactory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
        mqConnectionFactory.setStringProperty(WMQConstants.USERID, "app");
        mqConnectionFactory.setStringProperty(WMQConstants.PASSWORD, "passw0rd");

        System.setProperty("javax.net.ssl.keyStore", "/Users/tylerstanczak/mq/mq-spring/clientKeystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "clientpass");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.trustStore", "/Users/tylerstanczak/mq/mq-spring/clientkey.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "clientpass");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings","false");

        return mqConnectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(MQConnectionFactory mqConnectionFactory) {
        return new CachingConnectionFactory(mqConnectionFactory);
    }

    @Bean
    public JmsTemplate jmsTemplate(CachingConnectionFactory cachingConnectionFactory) {
        return new JmsTemplate(cachingConnectionFactory);
    }
}   