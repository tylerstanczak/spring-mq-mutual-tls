package com.example.mq_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@RestController
@EnableJms
public class MqSpringApplication {

	@Autowired
	private JmsTemplate jmsTemplate;

	@GetMapping("/send")
	String send() {
		try{
			jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World!");
			return "OK";
		}catch(JmsException ex){
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("/recv")
	String recv() {
		try{
			return jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
		} catch(JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@PostConstruct
	public String init() {
		try{
			jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World!");
		}catch(JmsException ex){
			ex.printStackTrace();
			return "FAIL";
		}
		try{
			String recvMsg = jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
			System.out.println("ALL OK: " + recvMsg);
			return "ALL OK";
		} catch(JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(MqSpringApplication.class, args);
	}

}
