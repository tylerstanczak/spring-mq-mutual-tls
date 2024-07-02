package com.example.mq_spring.config;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import jakarta.jms.JMSException;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean(name="factory1")
    public MQConnectionFactory mqConnectionFactory() throws JMSException {
        MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
        mqConnectionFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        mqConnectionFactory.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
        mqConnectionFactory.setHostName("localhost");
        mqConnectionFactory.setPort(1414);
        mqConnectionFactory.setQueueManager("QM1");
        mqConnectionFactory.setChannel("DEV.APP.SVRCONN");
        mqConnectionFactory.setSSLCipherSuite("*TLS12");
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

    @Bean(name="factory2")
    public JmsListenerContainerFactory<?> JmsListenerContainerFactory(MQConnectionFactory mqConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(mqConnectionFactory);
        return factory;
    }
}   