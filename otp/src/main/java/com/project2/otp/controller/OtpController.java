package com.project2.otp.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project2.otp.entity.EmailOtp;
import com.project2.otp.services.OtpService;

@RestController
public class OtpController {
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(OtpController.class);

	
	@Autowired
	OtpService services;
	
	@RequestMapping(method = RequestMethod.POST, value = "send_otp")
	public String sendOtp(@RequestParam String channel, @RequestParam String info) throws Exception
	{	
//		LOGGER.info("Inside send OTP");
		String ans;
		
		if(channel.equals("email"))
			ans = services.sendEmail(info);
		else
			ans = services.sendSMS(info);
		
		System.out.println(ans+"---------------------------------------------------------------------------------");
		return ans;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "validate_otp")
	public String validateOtp(@RequestBody EmailOtp obj) 
	{
//		LOGGER.info("Inside Validate OTP");
		System.out.println("----------");
		return services.validateOtp(obj);
	}
}
